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
 * BufferedBreedingPipeline.java
 * 
 * Created: December 28, 1999
 * By: Sean Luke
 */

/**
 * If empty, a 
 * BufferedBreedingPipeline makes a request of exactly <i>num-inds</i> 
 * individuals from a single child source; it then uses these
 * individuals to fill requests (returning min each time),
 * until the buffer is emptied, at
 * which time it grabs exactly <i>num-inds</i> more individuals, and so on.
 *
 * <p>What is this useful for?  Well, let's say for example that 
 * you want to cross over two individuals, then cross
 * them over again.  You'd like to hook up two CrossoverPipelines
 * in series.  Unfortunately, CrossoverPipeline takes
 * two sources; even if you set them to the same source, it requests
 * <i>one</i> individual from the first source and then <i>one</i>
 * from the second, where what you really want is for it to request
 * <i>two</i> individuals from a single source (the other CrossoverPipeline).
 * 
 * <p>The solution to this is to hook a CrossoverPipeline as the
 * source to a BufferedBreedingPipeline of buffer-size 2 (or some
 * multiple of 2 actually).  Then the BufferedBreedingPipeline is
 * set as both sources to another CrossoverPipeline.
 
 <p><b>Typical Number of Individuals Produced Per <tt>produce(...)</tt> call</b><br>
 1

 <p><b>Number of Sources</b><br>
 1
 
 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>num-inds</tt><br>
 <font size=-1>int &gt;= 1</font></td>
 <td valign=top>(the buffer size)</td></tr>
 </table>

 <p><b>Default Base</b><br>
 breed.buffered

 *
 * @author Sean Luke
 * @version 1.0 
 */

public class BufferedBreedingPipeline extends BreedingPipeline
    {
    public static final String P_BUFSIZE = "num-inds";
    public static final String P_BUFFERED = "buffered";
    public static final int INDS_PRODUCED = 1;
    public static final int NUM_SOURCES = 1;

    public Individual[] buffer;
    public int currentSize;

    public Parameter defaultBase()
        {
        return BreedDefaults.base().push(P_BUFFERED);
        }

    public int numSources() { return NUM_SOURCES; }    
    public int typicalIndsProduced() { return INDS_PRODUCED;}

    public void setup(final EvolutionState state, final Parameter base)
        {
        super.setup(state,base);

        Parameter def = defaultBase();

        int bufsize = state.parameters.getInt(base.push(P_BUFSIZE),
            def.push(P_BUFSIZE),1);
        if (bufsize == 0)
            state.output.fatal("BufferedBreedingPipeline's number of individuals must be >= 1.",base.push(P_BUFSIZE),def.push(P_BUFSIZE));
        
        buffer = new Individual[bufsize];
        currentSize=0; // just in case 

        // declare that likelihood isn't used
        if (likelihood < 1.0f)
            state.output.warning("BufferedBreedingPipeline does not respond to the 'likelihood' parameter.",
                base.push(P_LIKELIHOOD), def.push(P_LIKELIHOOD));
        }


    public void prepareToProduce(final EvolutionState state,
        final int subpopulation,
        final int thread)
        {
        super.prepareToProduce(state,subpopulation,thread);
        // reset my number of individuals to 0
        currentSize=0;
        }


    public int produce(final int min, 
        final int max, 
        final int start,
        final int subpopulation,
        final Individual[] inds,
        final EvolutionState state,
        final int thread) 

        {
        for(int q=start;q<min+start; q++ )
            {
            if (currentSize==0)         // reload
                {
                sources[0].produce(buffer.length,buffer.length,
                    0,subpopulation,buffer,state,thread);
                currentSize=buffer.length;
                
                // clone if necessary
                if (sources[0] instanceof SelectionMethod)
                    for(int z=0; z < buffer.length; z++)
                        buffer[z] = (Individual)(buffer[z].clone());
                }
            
            inds[q] = buffer[currentSize-1];
            currentSize--;
            }
        return min;
        }
    }
