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

import java.util.EventObject;

/**
 * @author spaus
 */
public class ParameterDatabaseEvent
    extends EventObject {

    public static final int SET = 0;
    public static final int ACCESSED = 1;
    
    private final Parameter parameter;
    private final String value;
    private final int type;

    /**
     * For ParameterDatabase events.
     * 
     * @param source the ParameterDatabase
     * @param parameter the Parameter associated with the event
     * @param value the value of the Parameter associated with the event
     * @param type the type of the event
     */
    public ParameterDatabaseEvent(Object source, Parameter parameter, String value, int type) {
        super(source);
        this.parameter = parameter;
        this.value = value;
        this.type = type;
        }
    
    /**
     * @return the Parameter associated with the event
     */
    public Parameter getParameter() {
        return parameter;
        }
    
    /**
     * @return the value of the Parameter associated with the event.
     */
    public String getValue() {
        return value;
        }
    
    /**
     * @return the type of the event.
     */
    public int getType() {
        return type;
        }
    }
