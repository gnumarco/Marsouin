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

/* 
 * Sextic.java
 * 
 * Created: Fri Nov 30 21:38:13 EST 2001
 * By: Sean Luke
 */

/**
 * Sextic implements a Symbolic Control problem.
 *
 * <p>The equation to be regressed is y = x^6 - 2x^4 + x^2, {x in [-1,1]}
 * <p>This equation was introduced in J. R. Koza, GP II, 1994.
 *
 */
public class Sextic extends Control
    {
    public double func(double x)
        { return x*x*x*x*x*x - 2.0*x*x*x*x + x*x; }
    }
