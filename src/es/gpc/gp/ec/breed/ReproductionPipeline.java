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
package es.gpc.gp.ec.breed;
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.SelectionMethod;
import es.gpc.gp.ec.BreedingPipeline;
import es.gpc.gp.ec.EvolutionState;

/* 
 * ReproductionPipeline.java
 * 
 * Created: Thu Nov  8 13:39:32 EST 2001
 * By: Sean Luke
 */

/**
 * ReproductionPipeline is a BreedingPipeline which simply makes a copy
 * of the individuals it recieves from its source.  If the source is another
 * BreedingPipeline, the individuals have already been cloned, so ReproductionPipeline
 * won't clone them again...unless you force it to do so by turning on the <tt>must-clone</tt>
 * parameter.
 *
 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 ...as many as the child produces

 <p><b>Number of Sources</b><br>
 1

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base.</i><tt>must-clone</tt><br>
 <font size=-1>bool =  <tt>true</tt> or <tt>false</tt> (default)</font></td>
 <td valign=top>(do we <i>always</i> clone our individuals, or only clone if the individual hasn't already been cloned by our source?  Typically you want <tt>false</tt>)</td></tr>

 </table>
 <p><b>Default Base</b><br>
 breed.reproduce

 * @author Sean Luke
 * @version 1.0 
 */

public class ReproductionPipeline extends BreedingPipeline
    {
    public static final String P_REPRODUCE = "reproduce";
    public static final String P_MUSTCLONE = "must-clone";
    public static final int NUM_SOURCES = 1;
    
    public boolean mustClone;
    
    public Parameter defaultBase() { return BreedDefaults.base().push(P_REPRODUCE); }

    public int numSources() { return NUM_SOURCES; }

    public void setup(final EvolutionState state, final Parameter base)
        {
        super.setup(state,base);
        Parameter def = defaultBase();
        mustClone = state.parameters.getBoolean(base.push(P_MUSTCLONE), def.push(P_MUSTCLONE),false);
                
        if (likelihood != 1.0)
            state.output.warning("ReproductionPipeline given a likelihood other than 1.0.  This is nonsensical and will be ignored.",
                base.push(P_LIKELIHOOD),
                def.push(P_LIKELIHOOD));
        }
        
    public int produce(final int min, 
        final int max, 
        final int start,
        final int subpopulation,
        final Individual[] inds,
        final EvolutionState state,
        final int thread) 
        {
        // grab individuals from our source and stick 'em right into inds.
        // we'll modify them from there
        int n = sources[0].produce(min,max,start,subpopulation,inds,state,thread);
                
        // this code is basically the same as BreedingPipeline.reproduce() but we copy it here
        // because of the 'mustClone' option.
                
        if (mustClone || sources[0] instanceof SelectionMethod)
            for(int q=start; q < n+start; q++)
                inds[q] = (Individual)(inds[q].clone());
        return n;
        }
    }
