/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package es.gpc.gp.ec.app.control.func;
import es.gpc.gp.ec.gp.ADFStack;
import es.gpc.gp.ec.gp.GPData;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.gp.GPNode;
import es.gpc.gp.ec.app.control.Control;
import es.gpc.gp.ec.app.control.ControlData;
import es.gpc.gp.ec.Problem;
import es.gpc.gp.ec.EvolutionState;

/* 
 * Add.java
 * 
 * Created: Wed Nov  3 18:26:37 1999
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class Root extends GPNode
    {
    public String toString() { return "Bound"; }
    

/*
  public void checkConstraints(final EvolutionState state,
  final int tree,
  final GPIndividual typicalIndividual,
  final Parameter individualBase)
  {
  super.checkConstraints(state,tree,typicalIndividual,individualBase);
  if (children.length!=2)
  state.output.error("Incorrect number of children for node " + 
  toStringForError() + " at " +
  individualBase);
  }
*/
    public int expectedChildren() { return 1; }


    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem)
        {
        double result;
        ControlData rd = ((ControlData)(input));
  
        children[0].eval(state,thread,input,stack,individual,problem);
        result = rd.x;
        if(rd.x<((Control)problem).lowBound)
            result = ((Control)problem).lowBound;
        if(rd.x>((Control)problem).upBound)
            result = ((Control)problem).upBound;
        rd.x = result;
        }
    }



