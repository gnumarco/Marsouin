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
import es.gpc.gp.ec.Population;
import es.gpc.gp.ec.BreedingPipeline;
import es.gpc.gp.ec.EvolutionState;

/* 
 * GPBreedingPipeline.java
 * 
 * Created: Tue Sep 14 19:38:09 1999
 * By: Sean Luke
 */

/**
 * A GPBreedingPipeline is a BreedingPipeline which produces only
 * members of some subclass of GPSpecies.   This is just a convenience
 * superclass for many of the breeding pipelines here; you don't have
 * to be a GPBreedingPipeline in order to breed GPSpecies or anything. 
 *
 * @author Sean Luke
 * @version 1.0 
 */

public abstract class GPBreedingPipeline extends BreedingPipeline 
    {
    /** Standard parameter for node-selectors associated with a GPBreedingPipeline */
    public static final String P_NODESELECTOR = "ns";

    /** Standard parameter for tree fixing */
    public static final String P_TREE = "tree";

    /** Standard value for an unfixed tree */
    public static final int TREE_UNFIXED = -1;


    /** Returns true if <i>s</i> is a GPSpecies. */
    public boolean produces(final EvolutionState state,
        final Population newpop,
        final int subpopulation,
        final int thread)
        {
        if (!super.produces(state,newpop,subpopulation,thread)) return false;

        // we produce individuals which are owned by subclasses of GPSpecies
        if (newpop.subpops[subpopulation].species instanceof GPSpecies)
            return true;
        return false;
        }

    }
