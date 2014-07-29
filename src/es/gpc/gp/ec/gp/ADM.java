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
package es.gpc.gp.ec.gp;
import es.gpc.gp.ec.Problem;
import es.gpc.gp.ec.EvolutionState;

/* 
 * ADM.java
 * 
 * Created: Tue Oct 26 15:29:57 1999
 * By: Sean Luke
 */

/**
 * An ADM is an ADF which doesn't evaluate its arguments beforehand, but
 * instead only evaluates them (and possibly repeatedly) when necessary
 * at runtime.  For more information, see ec.gp.ADF.
 * @see ec.gp.ADF
 *
 * @author Sean Luke
 * @version 1.0 
 */

public class ADM extends ADF
    {
    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem)
        {
        // prepare a context
        ADFContext c = stack.push(stack.get());
        c.prepareADM(this);
        
        // evaluate the top of the associatedTree
        individual.trees[associatedTree].child.eval(
            state,thread,input,stack,individual,problem);

        // pop the context off, and we're done!
        if (stack.pop(1) != 1)
            state.output.fatal("Stack prematurely empty for " + toStringForError());
        }
    }
