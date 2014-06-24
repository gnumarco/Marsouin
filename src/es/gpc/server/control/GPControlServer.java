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

import es.gpc.utils.Memory;
import es.gpc.utils.Message;
import es.gpc.gp.ec.Evolve;
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.util.ParameterDatabase;
import es.gpc.utils.GlobalLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marc
 */
public class GPControlServer implements es.gpc.generic.GPCApp {

    protected Memory m = new Memory();
    public Evolve e;
    GlobalLog glog = null;

//    GPControlServer() {
//        e = new Evolve(glog);
//        e.config(m, new String[]{"-file", "control.params"});
//    }
    
    GPControlServer(GlobalLog glob) {
        glog = glob;
        try {
            m.sensor.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(GPControlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        e = new Evolve(glog);
        e.config(m, new String[]{"-file", "control.params"});   
    }

    @Override
    public Message messageHandler(Message m) {

        Message mes = null;

        if (m.type.equalsIgnoreCase("config")) {
            glog.state = "Configuring the kernel";
            mes = configGP(m.config);
        }
        if (m.type.equalsIgnoreCase("start")) {
            glog.state = "Initializing the kernel";
            mes = initGP();
        }
        if (m.type.equalsIgnoreCase("fitness")) {
            mes = updateFitness(m.value);
        }
        if (m.type.equalsIgnoreCase("sensors")) {
            System.out.println("Sensors: " + m.value[0]);
            mes = updateSensors(m.value);
        }
        return mes;
    }

    public Message configGP(HashMap<String, String> map) {

        if (e.getState().equals(Thread.State.TERMINATED)) {
            System.out.println("Creating new instance of the GP kernel");
            e = new Evolve(glog);
            e.config(m, new String[]{"-file", "control.params"});
        }
        ParameterDatabase p = Evolve.loadParameterDatabase(new String[]{"-file", "control.params"});
        e.parameters = p;

        Iterator<Entry<String, String>> it = map.entrySet().iterator();
        for (Map.Entry<String, String> pairs : map.entrySet()) {
            //while (it.hasNext()) {
            //Map.Entry<String, String> pairs = it.next();
            e.parameters.set(new Parameter(pairs.getKey()), pairs.getValue());
            System.out.println("Set " + pairs.getKey() + " to " + pairs.getValue());
        }

        Message mes = new Message();
        mes.type = "info";
        mes.value = new double[]{0};
        return mes;

    }

    public Message initGP() {
        if (e.getState().equals(Thread.State.TERMINATED)) {
            System.out.println("Creating new instance of the GP kernel");
            e = new Evolve(glog);
            e.config(m, new String[]{"-file", "control.params"});
        }
        e.start();
        Message mes = new Message();
        mes.type = "info";
        mes.value = new double[]{0};
        return mes;

    }

    public Message updateFitness(double[] fit) {
        m.indivFinished = true;
        m.fit = fit[0];
        Message mes = new Message();
        mes.type = "fitness";
        mes.value = new double[]{m.fit};
        return mes;

    }

    public Message updateSensors(double[] sens) {
        // write the sensors to memory
        System.out.println("writing sensors to shared memory...");
        m.sens = sens;
        System.out.println("done");
        System.out.println("server releasing sensor");
        m.sensor.release();    
        try {
            // here wait to read the actuation from memory
            System.out.println("Server acquiring actu");
            m.actu.acquire();
            System.out.println("Actu acquired by server");
        } catch (InterruptedException ex) {
            Logger.getLogger(GPControlServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Message mes = new Message();
        mes.type = "actuation";
        mes.value = m.act;
        return mes;
    }

}
