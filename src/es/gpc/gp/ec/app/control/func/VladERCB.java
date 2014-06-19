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
package es.gpc.gp.ec.app.control.func;
import es.gpc.gp.ec.gp.ADFStack;
import es.gpc.gp.ec.gp.GPData;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.app.control.ControlData;
import es.gpc.gp.ec.Problem;
import es.gpc.gp.ec.EvolutionState;
import java.io.*;


/* 
 * VladERCB.java
 * 
 * Created: Wed Nov  3 18:26:37 1999
 * By: Sean Luke

 <p>This ERC appears all three the Vladislavleva function sets.  It is not a constant but rather a function of one parameter (n) with an internal constant (c) and returns n + c. Note that the value of c is drawn from the fully-closed range [-5.0, 5.0].

 <p>E. Vladislavleva, G. Smits, and D. Den Hertog. Order of Nonlinearity as a Complexity Measure for Models Generated by Symbolic Regression via Pareto Genetic Programming. <i>IEEE Trans EC,</i> 13(2):333-349, 2009.
*/

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class VladERCB extends VladERCA
    {
    public String name() { return "VladERCB"; }

    public String toStringForHumans()
        { return "n+" + (float)value; }

    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem)
        {
        ControlData rd = ((ControlData)(input));

        children[0].eval(state,thread,input,stack,individual,problem);
        rd.x = rd.x + value;
        }

    }



