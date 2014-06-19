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
package es.gpc.utils;

import java.util.HashMap;



/**
 *
 * @author marc
 */
public class Message {
    public String type;
    public double[] value;
    public HashMap<String, String> config;
    
    public void Message(){
        type = null;
        value = null;
        config = new HashMap<>();
    }
    
    public void message(String t, double[] v){
        type = t;
        value = v;
        config = new HashMap<>();
    }
    
}
