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
import es.gpc.gp.ec.simple.SimpleProblemForm;
import es.gpc.gp.ec.gp.koza.KozaFitness;
import es.gpc.gp.ec.gp.GPProblem;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.EvolutionState;
import java.io.*;
import java.util.*;
import es.gpc.utils.Memory;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Control extends GPProblem implements SimpleProblemForm {

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
    public String RelativePathToExecOrig = "";
    public String RelativePathToFilesOrig = "";
    public String fullPathToBaseDir = "";
    public String execName = "";
    public double upBound, lowBound;
    public boolean debug;
    private Memory mem;

    @Override
    public Object clone() {
        Control prob = (Control) (super.clone());

        prob.fullPathToBaseDir = this.fullPathToBaseDir;
        prob.RelativePathToFilesOrig = this.RelativePathToFilesOrig;
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

        // verify our input is the right class (or subclasses from it)
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
            ControlData input = (ControlData) (this.input);
            double desiredOut = 0d;

            int hits = 0;
            double sum = 0.0;

            while (!mem.indivFinished) {
                try {
                    System.out.println("Kernel acquiring sensor");
                    mem.sensor.acquire();
                    System.out.println("Sensor acquired by Kernel");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
                }
                currentValue = mem.sens;
                ((GPIndividual) ind).trees[0].child.eval(
                        state, threadnum, input, stack, ((GPIndividual) ind), this);
                mem.act = new double[]{input.x};
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
}
