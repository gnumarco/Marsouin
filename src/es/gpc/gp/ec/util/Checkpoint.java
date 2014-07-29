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
package es.gpc.gp.ec.util;
import es.gpc.gp.ec.EvolutionState;
import java.util.zip.*;
import java.io.*;

/* 
 * Checkpoint.java
 * 
 * Created: Tue Aug 10 22:39:19 1999
 * By: Sean Luke
 */

/**
 * Checkpoints ec.EvolutionState objects out to checkpoint files, or 
 * restores the same from checkpoint files.  Checkpoint take the following
 * form:
 *
 * <p><i>checkpointPrefix</i><tt>.</tt><i>generation</i><tt>.gz</tt>
 *
 * <p>...where <i>checkpointPrefix</i> is the checkpoing prefix given
 * in ec.EvolutionState, and <i>generation</i> is the current generation number
 * also given in ec.EvolutionState.
 * The ".gz" is added because the file is GZIPped to save space.
 *
 * <p>When writing a checkpoint file, if you have specified a checkpoint directory
 * in ec.EvolutionState.checkpointDirectory, then this directory will be used to
 * write the checkpoint files.  Otherwise they will be written in your working
 * directory (where you ran the Java process).
 *
 * @author Sean Luke
 * @version 1.1
 */

public class Checkpoint
    {

    /** Writes the evolution state out to a file. */

    public static void setCheckpoint(EvolutionState state)
        {
        try
            {
            File file = new File("" + state.checkpointPrefix + "." + state.generation + ".gz");
            
            if (state.checkpointDirectory != null)
                {
                file = new File(state.checkpointDirectory, 
                    "" + state.checkpointPrefix + "." + state.generation + ".gz");
                }
            ObjectOutputStream s = 
                new ObjectOutputStream(
                    new GZIPOutputStream (
                        new BufferedOutputStream(
                            new FileOutputStream(file))));
                
            s.writeObject(state);
            s.close();
            state.output.message("Wrote out checkpoint file " + 
                state.checkpointPrefix + "." + 
                state.generation + ".gz");
            }
        catch (IOException e)
            {
            state.output.warning("Unable to create the checkpoint file " + 
                state.checkpointPrefix + "." +
                state.generation + ".gz" + 
                "because of an IOException:\n--EXCEPTION--\n" +
                e + 
                "\n--EXCEPTION-END--\n");
            }
        }


    /** Returns an EvolutionState object read from a checkpoint file
        whose filename is <i>checkpoint</i> 
        *
        @exception java.lang.ClassNotFoundException thrown when the checkpoint file contains a class reference which doesn't exist in your class hierarchy.
    **/
    public static EvolutionState restoreFromCheckpoint(String checkpoint)
        throws IOException, ClassNotFoundException, OptionalDataException
    /* must throw something if error -- NEVER return null */
        { 
        // load from the file
        ObjectInputStream s = 
            new ObjectInputStream(
                new GZIPInputStream (
                    new BufferedInputStream (
                        new FileInputStream (checkpoint))));

        EvolutionState e = (EvolutionState) s.readObject();
        s.close();

        // restart from the checkpoint
    
        e.resetFromCheckpoint();
        return e; 
        }
    }

