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
 * DefaultsForm.java
 * 
 * Created: Fri Jan 21 15:14:01 2000
 * By: Sean Luke
 */

/**
 * DefaultsForm is the interface which describes how Defaults objects
 * should work.  In general there is one Defaults class for each
 * package (there doesn't have to be, but it would be nice).  This
 * class should be relatively uniquely named (the defaults class in
 * the GP package is called GPDefaults for example).
 * DefaultsForm objects should implement a single static final method:

 <p><tt>public final Parameter base();</tt>

 <p>...which returns the default parameter base for the package.  This
 method cannot be declared in this interface, however, because it is
 static.  :-)  So this interface isn't much use, except to describe how
 defaults objects should generally work.

 <p> A parameter base is a secondary "default" place for the parameters database to look
 for a parameter value if the primary value was not defined.

 *
 * @author Sean Luke
 * @version 1.0 
 */

public interface DefaultsForm 
    {
    }
