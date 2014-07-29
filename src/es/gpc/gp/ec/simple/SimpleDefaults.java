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
package es.gpc.gp.ec.simple;
import es.gpc.gp.ec.DefaultsForm;
import es.gpc.gp.ec.util.Parameter;

/* 
 * SimpleDefaults.java
 * 
 * Created: Thu Jan 20 17:19:12 2000
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public final class SimpleDefaults implements DefaultsForm 
    {
    public static final String P_SIMPLE = "simple";

    /** Returns the default base. */
    public static final Parameter base()
        {
        return new Parameter(P_SIMPLE);
        }
    }
