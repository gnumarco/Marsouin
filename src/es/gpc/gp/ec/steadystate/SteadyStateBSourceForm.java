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
package es.gpc.gp.ec.steadystate;

/* 
 * SteadyStateBSourceForm.java
 * 
 * Created: Sat Nov 20 17:00:18 1999
 * By: Sean Luke
 *
 * This form is required of all BreedingSources in a pipeline used in Steady-State Evolution.
 * It consists of two methods: <b>sourcesAreProperForm</b> which checks recursively to determine
 * that a BreedingPipeline's own sources are also using this interface; and <b>individualReplaced</b>,
 * which informs the BreedingSource that a new individual has entered into the population, displacing
 * another.  This is important because it may signal to SelectionMethods that they need to update their
 * statistics.
 *
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public interface SteadyStateBSourceForm
    {
    /** Called whenever an individual has been replaced by another
        in the population. */
    public void individualReplaced(final SteadyStateEvolutionState state,
        final int subpopulation,
        final int thread,
        final int individual);
    
    /** Issue an error (not a fatal -- we guarantee that callers
        of this method will also call exitIfErrors) if any
        of your sources, or <i>their</i> sources, etc., are not
        of SteadyStateBSourceForm.*/
    public void sourcesAreProperForm(final SteadyStateEvolutionState state);
    }
