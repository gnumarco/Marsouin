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
package es.gpc.gp.ec.spatial;

import es.gpc.gp.ec.coevolve.MultiPopCoevolutionaryEvaluator;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.SelectionMethod;
import es.gpc.gp.ec.EvolutionState;

/* 
 * SpatialMultiPopCoevolutionaryEvaluator.java
 * 
 * By: Liviu Panait and Sean Luke
 */

/** 
 * SpatialMultiPopCoevolutionaryEvaluator implements a coevolutionary evaluator involving multiple
 * spatially-embedded subpopulations.  You ought to use it in conjuction with SpatialTournamentSelection
 * (for selecting current-generation individuals, set the tournament selection size to 1, which will
 * pick randomly from the space).
 *
 * @author Liviu Panait Sean Luke
 * @version 2.0 
 */

public class SpatialMultiPopCoevolutionaryEvaluator extends MultiPopCoevolutionaryEvaluator
    {
    protected Individual produce(SelectionMethod method, int subpopulation, int individual, EvolutionState state, int thread)
        {
        if (!(state.population.subpops[subpopulation] instanceof Space))
            state.output.fatal("Subpopulation " + subpopulation + " is not a Space.");
                        
        Space space = (Space)(state.population.subpops[subpopulation]);
        space.setIndex(thread, individual);
                
        return state.population.subpops[subpopulation].individuals[method.produce(subpopulation, state, thread)];
        }
    }
