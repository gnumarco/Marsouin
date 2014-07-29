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
package es.gpc.generic;

import es.gpc.utils.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author marc
 */
public class GenericControlClient {

    URL url = null;
    URLConnection connection = null;
    OutputStreamWriter out = null;
    BufferedReader in = null;

    public double[] setSensorsGetActuation(double[] sens) throws IOException {
        connection = url.openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setDoOutput(true);

        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        //Create the Factory
        JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
        // Get the outputstream from the HTTP connection
        out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        try (JsonGenerator jg = jgf.createGenerator(out)) {
            jg.writeStartObject();
            jg.write("type", "sensors");
            jg.writeStartArray("values");
            for (int i = 0; i < sens.length; i++) {
                jg.writeStartObject();//     
                jg.write("value", sens[i]);
                jg.writeEnd();
            }
            jg.writeEnd();
            jg.writeEnd();
        }

        // Now we will read the answer
        // Getting the input stream from the HTTP connection
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        // Creating the JSON parser and associate it with the input stream
        JsonParser jr = Json.createParser(in);

        // Declare a variable of type JSON.Event
        JsonParser.Event event;

        // Create a new message that we will fill with the JSON text
        Message mes = new Message();

        // Read the first event
        event = jr.next();

        ArrayList<Double> arr = new ArrayList<>();
        while (event != JsonParser.Event.END_OBJECT) {
            switch (event) {
                case KEY_NAME: {
                    System.out.println(jr.getString());
                    if (jr.getString().equalsIgnoreCase("type")) {
                        jr.next();
                        mes.type = jr.getString();
                        System.out.println("Adding " + jr.getString());
                    }
                    break;
                }
                case VALUE_FALSE: {
                    System.out.println(false);
                    break;
                }
                case VALUE_NULL: {
                    System.out.println("null");
                    break;
                }
                case VALUE_NUMBER: {
                    if (jr.isIntegralNumber()) {
                        System.out.println(jr.getInt());
                    } else {
                        System.out.println("Adding " + jr.getBigDecimal());
                        arr.add(jr.getBigDecimal().doubleValue());
                    }
                    break;
                }
                case VALUE_STRING: {
                    System.out.println(jr.getString());
                    break;
                }
                case VALUE_TRUE: {
                    System.out.println(true);
                    break;
                }
                case START_OBJECT: {
                    System.out.println("START OBJECT");
                }
                default: {
                }
            }
            event = jr.next();
        }

        // Cast the ArrayList to an array of Double
        Double[] d = new Double[arr.size()];
        arr.toArray(d);
        // fill the "value" variable of the message with an array of double (!= array of Double --> use of toPrimitive(d))
        mes.value = ArrayUtils.toPrimitive(d);
        // Return the array of actuation values
        return mes.value;
    }

    // returns true if it is the last evaluation and we have to stop the system
    public boolean sendFitness(double fit) throws IOException {
        connection = url.openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setDoOutput(true);
        out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
        try (JsonGenerator jg = jgf.createGenerator(out)) {
            jg.writeStartObject();
            jg.write("type", "fitness");
            jg.writeStartArray("values");
            jg.writeStartObject();
            jg.write("value", fit);
            jg.writeEnd();
            jg.writeEnd();
            jg.writeEnd();
        }

        //read the answer: should we stop the system?
        boolean stop = false;
        in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        JsonParser jr = Json.createParser(in);

        // Declare a variable of type JSON.Event
        JsonParser.Event event;

        // Create a new message that we will fill with the JSON message
        Message mes = new Message();

        // Read the first event
        event = jr.next();

        ArrayList<Double> arr = new ArrayList<>();
        while (event != JsonParser.Event.END_OBJECT) {
            switch (event) {
                case KEY_NAME: {
                    System.out.println(jr.getString());
                    if (jr.getString().equalsIgnoreCase("type")) {
                        jr.next();
                        mes.type = jr.getString();
                        System.out.println("Adding " + jr.getString());
                    }
                    break;
                }
                case VALUE_FALSE: {
                    System.out.println(false);
                    break;
                }
                case VALUE_NULL: {
                    System.out.println("null");
                    break;
                }
                case VALUE_NUMBER: {
                    if (jr.isIntegralNumber()) {
                        System.out.println(jr.getInt());
                    } else {
                        System.out.println("Adding " + jr.getBigDecimal());
                        arr.add(jr.getBigDecimal().doubleValue());
                    }
                    break;
                }
                case VALUE_STRING: {
                    System.out.println(jr.getString());
                    break;
                }
                case VALUE_TRUE: {
                    System.out.println(true);
                    break;
                }
                case START_OBJECT: {
                    System.out.println("START OBJECT");
                }
                default: {
                }
            }
            event = jr.next();
        }

        // Cast the ArrayList to an array of Double
        Double[] d = new Double[arr.size()];
        arr.toArray(d);
        // fill the "value" variable of the message with an array of double (!= array of Double --> use of toPrimitive(d))
        mes.value = ArrayUtils.toPrimitive(d);

        if (mes.type.equalsIgnoreCase("fitness")) {
            System.out.println("Fitness: " + mes.value[0]);
            if (mes.value[0] == 1d) {
                stop = true;
            }
        }
        return stop;
    }

    public void setURL(String u) throws MalformedURLException, IOException {
        url = new URL(u);
    }
}
