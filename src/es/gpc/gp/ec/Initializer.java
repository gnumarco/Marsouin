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
 * Initializer.java
 * 
 * Created: Tue Aug 10 21:07:42 1999
 * By: Sean Luke
 */

/**
 * The Initializer is a singleton object whose job is to initialize the
 * population at the beginning of the run.  It does this by providing
 * a population through the initialPopulation(...) method.

 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><tt>pop</tt><br>
 <font size=-1>classname, inherits or = ec.Population</font></td>
 <td valign=top>(the class for a new population)</td></tr>
 </table>

 <p><b>Parameter bases</b><br>
 <table>
 <tr><td valign=top><tt>pop</tt></td>
 <td>The base for a new population's set up parameters</td></tr>
 </table>

 * @author Sean Luke
 * @version 1.0 
 */

public abstract class Initializer implements Singleton
    {
    /** parameter for a new population */
    public static final String P_POP = "pop";

    /** Creates and returns a new initial population for the evolutionary run.
        This is commonly done by setting up a Population (by calling setupPopulation below)
        then calling its populate() method.  This method
        will likely only be called once in a run. */
    public abstract Population initialPopulation(final EvolutionState state, int thread);
        
    /** Loads a Population from the parameter file, sets it up, and returns it. */
    public abstract Population setupPopulation(final EvolutionState state, int thread); 
    }
