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
import es.gpc.gp.ec.gp.GPData;

/* 
 * ControlData.java
 * 
 * Created: Wed Nov  3 18:32:13 1999
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class ControlData extends GPData
    {
    // return value
    public double x;
    public double[] b;

    public void copyTo(final GPData gpd) 
        { ((ControlData)gpd).x = x;
        ((ControlData)gpd).b = b;}
    }
