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
import es.gpc.gp.ec.util.Parameter;
import java.io.Serializable;

/* 
 * Setup.java
 * 
 * Created: Mon Oct  4 17:15:44 1999
 * By: Sean Luke
 */

/**
 * Setup classes are classes which get set up once from user-supplied parameters
 * prior to being used.
 *
 * Defines a single method, setup(...), which is called at least once for the
 * object, or for some object from which it was cloned.  This method
 * allows the object to set itself up by reading from parameter lists and
 * files on-disk.  You may assume that this method is called in a non-threaded
 * environment, hence your thread number is 0 (so you can determine which
 * random number generator to use).
 *
 * @author Sean Luke
 * @version 1.0 
 */

public strictfp interface Setup extends Serializable
    {
    /** Sets up the object by reading it from the parameters stored
        in <i>state</i>, built off of the parameter base <i>base</i>.
        If an ancestor implements this method, be sure to call
        super.setup(state,base);  before you do anything else. */
    public void setup(final EvolutionState state, final Parameter base);
    }
