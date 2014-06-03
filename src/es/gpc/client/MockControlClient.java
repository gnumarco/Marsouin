/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.gpc.client;

/**
 *
 * @author marc
 */
import java.io.*;

public class MockControlClient extends es.gpc.generic.GenericControlClient {

    public void startDSystemForLearning() throws IOException {
        boolean stop = false;
        double[] act;
        double[] sens;      
        while (!stop) {
            // reinit the DSystem here
            // Eval the indiv
            long tot = 0;
            long duration = 0;
            for (int i = 0; i < 1000; i++) {
                sens = new double[]{0.1, 0.2};
                final long startTime = System.nanoTime();
                act = setSensorsGetActuation(sens);
                duration = System.nanoTime() - startTime;
                tot += duration;
                System.out.println("actuation recieved in "+duration/1000+"µs : ");
                for (int j = 0; j < act.length; j++) {
                    System.out.println(act[j]);
                }
            }
            System.out.println("mean comm time: "+tot/1000/1000+"µs");
            System.out.println("tot comm time: "+tot/1000/1000+"ms");
            // get the fitness and check if we stop the system
            stop = sendFitness(0.1);
        }
    }

}
