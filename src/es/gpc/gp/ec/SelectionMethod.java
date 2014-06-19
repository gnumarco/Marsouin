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
package es.gpc.gp.ec;

/* 
 * SelectionMethod.java
 * 
 * Created: Mon Aug 30 19:19:56 1999
 * By: Sean Luke
 */

/**
 * A SelectionMethod is a BreedingSource which provides direct IMMUTABLE pointers
 * to original individuals in an old population, not fresh mutable copies.
 * If you use a SelectionMethod as your BreedingSource, you must 
 * SelectionMethods might include Tournament Selection, Fitness Proportional Selection, etc.
 * SelectionMethods don't have parent sources.
 *
 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 Always 1.

 * @author Sean Luke
 * @version 1.0 
 */

public abstract class SelectionMethod extends BreedingSource
    {
    public static final int INDS_PRODUCED = 1;

    /** Returns 1 (the typical default value) */
    public int typicalIndsProduced() { return INDS_PRODUCED; }

    /** A default version of produces -- this method always returns
        true under the assumption that the selection method works
        with all Fitnesses.  If this isn't the case, you should override
        this to return your own assessment. */
    public boolean produces(final EvolutionState state,
        final Population newpop,
        final int subpopulation,
        final int thread)
        {
        return true;
        }


    /** A default version of prepareToProduce which does nothing.  */
    public void prepareToProduce(final EvolutionState s,
        final int subpopulation,
        final int thread)
        { return; }

    /** A default version of finishProducing, which does nothing. */
    public void finishProducing(final EvolutionState s,
        final int subpopulation,
        final int thread)
        { return; }

    public int produce(final int min, 
        final int max, 
        final int start,
        final int subpopulation,
        final Individual[] inds,
        final EvolutionState state,
        final int thread) 
        {
        int n=INDS_PRODUCED;
        if (n<min) n = min;
        if (n>max) n = max;
        
        for(int q=0;q<n;q++)
            inds[start+q] = state.population.subpops[subpopulation].
                individuals[produce(subpopulation,state,thread)];
        return n;
        }
    
    /** An alternative form of "produce" special to Selection Methods;
        selects an individual from the given subpopulation and 
        returns its position in that subpopulation. */
    public abstract int produce(final int subpopulation,
        final EvolutionState state,
        final int thread);
    }



