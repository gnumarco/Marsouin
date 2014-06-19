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
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.Prototype;
import es.gpc.gp.ec.EvolutionState;

/* 
 * GPData.java
 * 
 * Created: Mon Oct 25 19:37:18 1999
 * By: Sean Luke
 */

/**
 * GPData is the parent class of data transferred between GPNodes.
 * If performed correctly, there need be only one GPData instance 
 * ever created in the evaluation of many individuals. 
 *
 * <p>You can use GPData as-is if you have absolutely no data to
 * transfer between individuals.  Otherwise, you need to subclas
 * GPData, add your own instance variables, and then override
 * the copyTo(...) method and, depending on whether the data has
 * pointers in it (like arrays), the clone() method as well.

 <p><b>Default Base</b><br>
 gp.data

 *
 * @author Sean Luke
 * @version 1.0 
 */

public class GPData implements Prototype
    {
    public static final String P_GPDATA = "data";

    /** Modifies gpd so that gpd is equivalent to us. You may
        safely assume that gpd is of the same class as we are.
        Do not share pointers with the other object, except to
        read-only data: instead, copy any read-write data as necessary. */
    public void copyTo(final GPData gpd)
        {
        }

    public Parameter defaultBase()
        {
        return GPDefaults.base().push(P_GPDATA);
        }

    public void setup(final EvolutionState state, final Parameter base)
        {
        }

    public Object clone()
        {
        try { return super.clone(); }
        catch (CloneNotSupportedException e) 
            { throw new InternalError(); } // never happens
        }
    }
