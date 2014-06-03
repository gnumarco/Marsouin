/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.gpc.server.control;

import es.gpc.utils.Memory;
import es.gpc.utils.Message;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.Evolve;
import static es.gpc.gp.ec.Evolve.loadParameterDatabase;
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.util.ParameterDatabase;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marc
 */
public class GPControlServer implements es.gpc.generic.AMBApp {

    protected Memory m = new Memory();
    protected Evolve e = new Evolve();

    GPControlServer() {
        e.config(m, new String[]{"-file", "control.params"});
    }

    @Override
    public Message messageHandler(Message m) {

        Message mes = null;

        if (m.type.equalsIgnoreCase("init")) {
            mes = configGP(m.config);
        }
        if (m.type.equalsIgnoreCase("start")) {
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

        
        ParameterDatabase p = Evolve.loadParameterDatabase(new String[]{"-file", "control.params"});
        e.parameters = p;
        
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry) it.next();
            e.parameters.set(new Parameter(pairs.getKey()), pairs.getValue());
            System.out.println("Set "+pairs.getKey() + " to " + pairs.getValue());
        }

        Message mes = new Message();
        mes.type = "info";
        mes.value = new double[]{0};
        return mes;

    }

    public Message initGP() {
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
        mes.value = new double[]{0};
        return mes;

    }

    public Message updateSensors(double[] sens) {
        // write the sensors to memory
        System.out.println("writing sensors to shared memory...");
        m.sens = sens;
        m.newSens = true;
        System.out.println("done");
        // here wait to read the actuation from memory
        while (!m.newAct) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(GPControlServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Message mes = new Message();
        mes.type = "actuation";
        mes.value = m.act;
        m.newAct = false;
        return mes;
    }

}
