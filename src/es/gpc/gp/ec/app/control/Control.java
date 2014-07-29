/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package es.gpc.gp.ec.app.control;

import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.gp.koza.KozaFitness;
import es.gpc.gp.ec.gp.GPProblem;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.app.control.func.RegERC;
import es.gpc.gp.ec.gp.GPFunctionSet;
import es.gpc.gp.ec.gp.GPInitializer;
import es.gpc.gp.ec.gp.GPNode;
import es.gpc.gp.ec.gp.GPTree;
import es.gpc.utils.Memory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Control extends GPProblem{

    private static final long serialVersionUID = 1;
    public static final String P_SIZE = "sensors";
    public static final String P_GROUP = "groups";
    public static final String P_FILE = "file";
    public static final String P_USE_FUNCTION = "use-function";
    public static final String P_EXEC_NAME = "exec-name";
    public static final String P_BASE_DIR = "baseFiles";
    public static final String P_DATA_DIR = "dataFiles";
    public static final String P_EXEC_DIR = "execFiles";
    public static final String P_DEBUG = "debug";
    public double[] currentValue;
    public int sensors;
    public String relativePathToExecOrig = "";
    public String relativePathToFilesOrig = "";
    public String fullPathToBaseDir = "";
    public String execName = "";
    public double upBound, lowBound;
    public boolean debug;
    private Memory mem;

    @Override
    public Object clone() {
        Control prob = (Control) (super.clone());

        prob.fullPathToBaseDir = this.fullPathToBaseDir;
        prob.relativePathToFilesOrig = this.relativePathToFilesOrig;
        prob.execName = this.execName;
        prob.upBound = this.upBound;
        prob.lowBound = this.lowBound;
        prob.debug = this.debug;
        prob.sensors = this.sensors;
        return prob;
    }

    @Override
    public void setup(final EvolutionState state,
            final Parameter base) {
        // very important, remember this
        super.setup(state, base);

        // verify our inputLocal is the right class (or subclasses from it)
        if (!(input instanceof ControlData)) {
            state.output.fatal("GPData class must subclass from " + ControlData.class,
                    base.push(P_DATA), null);
        }

        debug = state.parameters.getBoolean(base.push(P_DEBUG), null, true);
        if (debug) {
            state.output.message("We are in DEBUG mode");
        }

        mem = ((ControlEvolutionState) state).memory;

        sensors = state.parameters.getInt(base.push(P_SIZE), null, 1);
        if (sensors < 1) {
            state.output.fatal("Number of sensors must be an integer greater than 0", base.push(P_SIZE));
        }

        // Get the upper and lower boundaries for the output
        upBound = state.parameters.getDouble(base.push("upBound"), null);
        lowBound = state.parameters.getDouble(base.push("lowBound"), null);
        state.output.message("We work between " + lowBound + " and " + upBound);

        // We controlled and set up all paremeters, lets set up multithreading
        state.output.message("we work with " + state.evalthreads + " evaluation threads.");

    }

    @Override
    public void evaluate(final EvolutionState state,
            final Individual ind,
            final int subpopulation,
            final int threadnum) {
        if (!ind.evaluated) // don't bother reevaluating
        {
            ControlData inputLocal = (ControlData) (this.input);

            int hits = 0;

            while (!mem.indivFinished) {
                try {
                    System.out.println("Kernel acquiring sensor");
                    mem.sensor.acquire();
                    System.out.println("Sensor acquired by Kernel");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                }
                currentValue = mem.sens;
                ((GPIndividual) ind).trees[0].child.eval(state, threadnum, inputLocal, stack, ((GPIndividual) ind), this);
                mem.act = new double[]{inputLocal.x};
                System.out.println("Kernel releasing actu");
                mem.actu.release();
            }
            
            // the fitness better be KozaFitness!
            KozaFitness f = ((KozaFitness) ind.fitness);
            f.setStandardizedFitness(state, (float) mem.fit);
            mem.indivFinished = false;
            f.hits = hits;
            //ind.evaluated = true;
        }
    }
    
    public void simplifyTrees(final EvolutionState state,
            final Individual ind,
            final int subpopulation,
            final int threadnum) {

        // loop on all the trees (we usually only have 1)
        for (GPTree tree : ((GPIndividual) ind).trees) {
            if (tree.child.expectedChildren() > 0) {
                //Retrieving the ERC "factory" --> Ugly. to test!!!!!!!
                //  Retrieveing the initializer and the functionset to be able to create new ERCs 
                GPInitializer initializer = ((GPInitializer) state.initializer);
                GPFunctionSet set = tree.constraints(initializer).functionset;
                int index = 0, index2 = 0;
                int i = 0;
                for (GPNode[] tab : set.terminals) {
                    int j = 0;
                    for (GPNode gpn : tab) {
                        //state.output.message(((GPNode) gpn).name());
                        if (((GPNode) gpn).name().equalsIgnoreCase("ERC")) {
                            index = i;
                            index2 = j;
                        }
                        j++;
                    }
                    i++;
                }
                RegERC ERCFact = (RegERC) set.terminals[index][index2];
                // Here we want to go down the tree and see if we can simplify 
                //state.output.message("Entering Constant simplification for individual ".concat(tree.child.makeLispTree()));
                simplifyTree(state, tree.child, subpopulation, threadnum, tree, (byte) 0, ERCFact);
                //state.output.message("Simplified individual: ".concat(tree.child.makeLispTree()));
            }
        }

    }

    public void simplifyTree(final EvolutionState state,
            final GPNode n,
            final int subpopulation,
            final int threadnum, GPTree tree, byte position, RegERC fact) {

        ControlData inputData = (ControlData) (this.input);
        GPNode node = n;
        // We want to test if all the children of the node are ERCs
        boolean allERC = true;
        for (GPNode nd : node.children) {
            if (!nd.name().equalsIgnoreCase("ERC")) {
                allERC = false;
            }
        }

        // Here starts the simplification. We will go up the Tree as long as we have nodes with all ERC children
        while (allERC) {

            //simplify
            //state.output.message("Simplifying: ".concat(node.toString()));
            // We calculate the value of the subtree
            node.eval(state, threadnum, inputData, stack, null, this);
            // Now we will replace this subtree by an ERC of the same value

            GPNode newERC = fact.lightClone();
            newERC.resetNode(state, threadnum);
            ((RegERC) newERC).value = inputData.x;
            newERC.argposition = position;
            node.replaceWithNoChildren(newERC);
            if (!(node.parent instanceof GPNode)) {
                allERC = false;
            } else {
                node = (GPNode) node.parent;
                allERC = true;
                for (GPNode nd : node.children) {
                    if (!nd.name().equalsIgnoreCase("ERC")) {
                        allERC = false;
                    }
                }
            }

        }

        // Lets see the children
        for (byte i = 0; i < node.children.length; i++) {
            if (node.children[i].expectedChildren() > 0) {
                simplifyTree(state, node.children[i], subpopulation, threadnum, tree, i, fact);
            }
        }

    }
}
