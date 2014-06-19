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
package es.gpc.gp.ec.steadystate;

/* 
 * SteadyStateExchangerForm
 * 
 * Created: Tue Aug 10 21:59:17 1999
 * By: Sean Luke
 */

/**
 * The SteadyStateExchangerForm is a badge which Exchanger subclasses
 * may wear if they work properly with the SteadyStateEvolutionState
 * mechanism.  The basic thing such classes must remember to do is:
 * Remember to call state.breeder.individualsReplaced(...) if
 * you modify or replace any individuals in a subpopulation.  Also,
 * realize that any individuals you exchange in will not be checked
 * to see if they're the ideal individual
 * @author Sean Luke
 * @version 1.0 
 */

public interface SteadyStateExchangerForm
    {
    }
