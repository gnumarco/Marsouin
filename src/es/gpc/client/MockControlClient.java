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
