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
package es.gpc.gp.ec.select;
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.steadystate.SteadyStateEvolutionState;
import es.gpc.gp.ec.steadystate.SteadyStateBSourceForm;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.SelectionMethod;
import es.gpc.gp.ec.EvolutionState;

/* 
 * RandomSelection.java
 * 
 * Created: Tue Sep 3 2002
 * By: Liviu Panait
 */

/**
 * Picks a random individual in the subpopulation.  This is mostly
 * for testing purposes.
 *

 <p><b>Default Base</b><br>
 select.random

 *
 * @author Sean Luke
 * @version 1.0 
 */

public class RandomSelection extends SelectionMethod implements SteadyStateBSourceForm
    {
    /** default base */
    public static final String P_RANDOM = "random";

    public Parameter defaultBase()
        {
        return SelectDefaults.base().push(P_RANDOM);
        }

    // I hard-code both produce(...) methods for efficiency's sake

    public int produce(final int subpopulation,
        final EvolutionState state,
        final int thread)
        {
        return state.random[thread].nextInt( state.population.subpops[subpopulation].individuals.length );
        }

    // I hard-code both produce(...) methods for efficiency's sake

    public int produce(final int min, 
        final int max, 
        final int start,
        final int subpopulation,
        final Individual[] inds,
        final EvolutionState state,
        final int thread) 
        {
        int n = 1;
        if (n>max) n = max;
        if (n<min) n = min;

        for(int q = 0; q < n; q++)
            {
            Individual[] oldinds = state.population.subpops[subpopulation].individuals;
            inds[start+q] = oldinds[state.random[thread].nextInt( state.population.subpops[subpopulation].individuals.length )];
            }
        return n;
        }

    public void individualReplaced(final SteadyStateEvolutionState state,
        final int subpopulation,
        final int thread,
        final int individual)
        { return; }
    
    public void sourcesAreProperForm(final SteadyStateEvolutionState state)
        { return; }
    
    }
