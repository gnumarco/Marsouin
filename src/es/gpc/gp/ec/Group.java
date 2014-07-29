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
 * Group.java
 * 
 * Created: Tue Aug 10 20:49:45 1999
 * By: Sean Luke
 */

/**
 * Groups are used for populations and subpopulations.  They are slightly
 * different from Prototypes in a few important ways.
 *
 * A Group instance typically is set up with setup(...) and then <i>used</i>
 * (unlike in a Prototype, where the prototype instance is never used, 
 * but only makes clones
 * which are used).  When a new Group instance is needed, it is created by
 * calling emptyClone() on a previous Group instance, which returns a
 * new instance set up exactly like the first Group instance had been set up
 * when setup(...) was called on it.
 *
 * Groups are Serializable and Cloneable, but you should not clone
 * them -- use emptyClone instead.
 *
 *
 * @author Sean Luke
 * @version 1.0 
 */

public interface Group extends Setup, Cloneable
    {
    /** Returns a copy of the object just as it had been 
        immediately after Setup was called on it (or on
        an ancestor object).  You can obtain a fresh instance
        using clone(), and then modify that.
    */
    public Group emptyClone();
    }
