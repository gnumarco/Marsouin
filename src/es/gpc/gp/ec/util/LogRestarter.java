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
import java.io.IOException;
import java.io.Serializable;

/* 
 * LogRestarter.java
 * 
 * Created: Wed Aug 11 14:55:03 1999
 * By: Sean Luke
 */

/**
 * A LogRestarter is an abstract superclass of objects which are
 * capable of restarting logs after a computer failure.   
 * LogRestarters subclasses are generally used
 * internally in Logs only; you shouldn't need to deal with them.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public abstract class LogRestarter implements Serializable
    {
    /* recreate the writer for, and properly reopen a log
       upon a system restart from a checkpoint */
    public abstract Log restart(Log l) throws IOException;

    /* close an existing log file and reopen it (non-appending),
       if that' appropriate for this kind of log.  Otherwise,
       don't do anything. */
    public abstract Log reopen(Log l) throws IOException;
    }
