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
import es.gpc.gp.ec.DefaultsForm;
import es.gpc.gp.ec.util.Parameter;

/* 
 * GPDefaults.java
 * 
 * Created: Tue Oct 12 17:44:47 1999
 * By: Sean Luke
 */

/**
 * A static class that returns the base for "default values" which GP-style
 * operators use, rather than making the user specify them all on a per-
 * species basis.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public final class GPDefaults implements DefaultsForm
    {
    public static final String P_GP = "gp";

    /** Returns the default base. */
    public static final Parameter base()
        {
        return new Parameter(P_GP);
        }
    }
