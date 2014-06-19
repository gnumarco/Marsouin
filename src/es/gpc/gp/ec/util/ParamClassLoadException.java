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
package es.gpc.gp.ec.util;

/* 
 * ParamClassLoadException.java
 * 
 * Created: Tue Aug 10 21:22:08 1999
 * By: Sean Luke
 */

/**
 * This exception is thrown by the Parameter Database when it fails to
 * locate and load a class specified by a given parameter as requested.
 * Most commonly this results in the program exiting with an error, so
 * it is defined as a RuntimeException so you don't have to catch it
 * or declare that you throw it.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public class ParamClassLoadException extends RuntimeException
    {
    public ParamClassLoadException(String s)
        { super("\n"+s); }
    }
