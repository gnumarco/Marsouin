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
package es.gpc.gp.ec.simple;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.EvolutionState;

/* 
 * SimpleProblemForm.java
 * 
 * Created: Tue Mar  6 11:33:37 EST 2001
 * By: Sean Luke
 */

/**
 * SimpleProblemForm is an interface which defines methods
 * for Problems to implement simple, single-individual (non-coevolutionary)
 * evaluation.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public interface SimpleProblemForm
    {
    /** Evaluates the individual in ind, if necessary (perhaps
        not evaluating them if their evaluated flags are true),
        and sets their fitness appropriately. 
    */

    public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum);

    /** "Reevaluates" an individual,
        for the purpose of printing out
        interesting facts about the individual in the context of the
        Problem, and logs the results.  This might be called to print out 
        facts about the best individual in the population, for example.  */
    
    public void describe(
        final EvolutionState state, 
        final Individual ind, 
        final int subpopulation,
        final int threadnum,
        final int log);
    }
