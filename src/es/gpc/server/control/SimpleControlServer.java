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
package es.gpc.server.control;

import es.gpc.utils.Message;

/**
 *
 * @author marc
 */
public class SimpleControlServer implements es.gpc.generic.GPCApp {

    @Override
    public Message messageHandler(Message m) {

        Message mes = null;
        System.out.println("Got message: "+m.type);

        if (m.type.equalsIgnoreCase("fitness")) {
            mes = updateFitness(m.value);
        }
        if (m.type.equalsIgnoreCase("sensors")) {
            mes = setSensorsGetActuation(m.value);
        }
        return mes;
    }

    public Message updateFitness(double[] v) {
        Message m = new Message();
        m.type = "fitness";
        m.value = new double[] {1};
        
        return m;
    }

    public Message setSensorsGetActuation(double[] v) {
        Message m =  new Message();
        m.type = "actuation";
        m.value = new double[] {0,0.5};

        return m;
    }

}
