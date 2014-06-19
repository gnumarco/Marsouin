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

/* 
 * GPAtomicType.java
 * 
 * Created: Fri Aug 27 21:16:45 1999
 * By: Sean Luke
 */

/**
 * A GPAtomicType is a simple, atomic GPType.  For more information, see GPType.
 @see ec.gp.GPType
 *
 * @author Sean Luke
 * @version 1.0 
 */

public final class GPAtomicType extends GPType
    {
    /** Use this constructor for GPAtomic Type unless you know what you're doing */
    public GPAtomicType(final String n) { name = n; }

    /** Don't use this constructor unless you call setup(...) immediately after it. */
    public GPAtomicType() { }

    public final boolean compatibleWith(final GPInitializer initializer, final GPType t)
        {
        // if the type is me, then I'm compatible with it
        if (t.type==type) return true;
        
        // if the type an atomic type, then return false
        else if (t.type < initializer.numAtomicTypes) return false;
        
        // if the type is < 0 (it's a set type), then I'm compatible
        // if I'm contained in it.  Use its sparse array.
        else return ((GPSetType)t).types_sparse[type];
        }
    }
