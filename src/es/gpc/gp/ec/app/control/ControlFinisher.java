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
package es.gpc.gp.ec.app.control;

import es.gpc.gp.ec.Finisher;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.util.Parameter;
import java.io.*;

/* 
 * SimpleFinisher.java
 * 
 * Created: Tue Aug 10 21:09:18 1999
 * By: Sean Luke
 */
/**
 * SimpleFinisher is a default Finisher which doesn't do anything.  Most
 * application's don't need Finisher facilities, so this version will work
 * fine.
 *
 * @author Sean Luke
 * @version 1.0 
 */
public class ControlFinisher extends Finisher {

    Parameter base;

    @Override
    public void setup(final EvolutionState state, final Parameter base) {
        this.base = base;
    }

    /** Doesn't do anything. */
    @Override
    public void finishPopulation(final EvolutionState state, final int result) {
        // don't care
        state.output.message("Writing endfile to: GPFinished");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("GPFinished", "UTF-8");
            writer.println("The End ;)");

        } catch (Exception e) {
            state.output.fatal("Failed to write endfile");
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                state.output.fatal("Failed to close endfile");
            }
        }
    }
}
