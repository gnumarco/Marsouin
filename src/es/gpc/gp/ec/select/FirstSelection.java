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
 * FirstSelection.java
 * 
 * Created: Mon Aug 30 19:27:15 1999
 * By: Sean Luke
 */

/**
 * Always picks the first individual in the subpopulation.  This is mostly
 * for testing purposes.
 *

 <p><b>Default Base</b><br>
 select.first

 *
 * @author Sean Luke
 * @version 1.0 
 */

public class FirstSelection extends SelectionMethod implements SteadyStateBSourceForm
    {
    /** default base */
    public static final String P_FIRST = "first";

    public Parameter defaultBase()
        {
        return SelectDefaults.base().push(P_FIRST);
        }
    
    // I hard-code both produce(...) methods for efficiency's sake

    public int produce(final int subpopulation,
        final EvolutionState state,
        final int thread)
        {
        return 0;
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
            // pick size random individuals, then pick the best.
            Individual[] oldinds = state.population.subpops[subpopulation].individuals;
            inds[start+q] = oldinds[0];  // note it's a pointer transfer, not a copy!
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
