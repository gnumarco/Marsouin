/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.gpc.server.control;


import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.json.Json;
import javax.json.*;

import es.gpc.utils.Message;

import java.util.HashMap;
import java.util.Map;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author marc
 */
public class ControlSocketHandler extends Thread {

    private String greeting = "Hello World";
    private final es.gpc.generic.AMBApp app;
    private InputStream is;
    private OutputStream out;

    public ControlSocketHandler(InputStream i, OutputStream o) {
        app = new SimpleControlServer();
        is = i;
        out = o;
    }

    public ControlSocketHandler(String greeting) {
        this.greeting = greeting;
        app = new SimpleControlServer();
    }

    @Override
    public void run() {
        BufferedReader in = null;
   
        Message mes = new Message();
        try {
            

            JsonReader rdr = Json.createReader(is);

            JsonObject obj = rdr.readObject();
            System.out.println("Starting reading Json message");
            String t = obj.getString("type");
            mes.type = obj.getString("type");
            JsonArray values = obj.getJsonArray("values");
            double[] d = new double[values.size()];
            JsonObject result;
            System.out.println("type : " + t);
            for (int i = 0; i < values.size(); i++) {
                result = values.getJsonObject(i);
                System.out.println("value : " + result.getJsonNumber("value"));
                d[i] = result.getJsonNumber("value").doubleValue();
            }
            mes.value = d;

            // Streaming model
//            
//            ArrayList<Double> arr = new ArrayList<>();
//            try (ServletInputStream sin = req.getInputStream()) {
//                JsonParser jr = Json.createParser(sin);
//                JsonParser.Event event;
//
//                String inString = new String(input);
//                System.out.println(inString);
//                event = jr.next();
//
//                // Output contents of "address" object
//                while (event != JsonParser.Event.END_OBJECT) {
//                    switch (event) {
//                        case KEY_NAME: {
//                            System.out.println(jr.getString() + " ");
//                            if (jr.getString().equalsIgnoreCase("type")) {
//                                jr.next();
//                                mes.type = jr.getString();
//                                System.out.println(jr.getString());
//                                //System.out.println("Adding " + jr.getString());
//                            }
//                            break;
//                        }
//                        case VALUE_FALSE: {
//                            System.out.println(false);
//                            break;
//                        }
//                        case VALUE_NULL: {
//                            System.out.println("null");
//                            break;
//                        }
//                        case VALUE_NUMBER: {
//                            if (jr.isIntegralNumber()) {
//                                System.out.println(jr.getInt());
//                            } else {
//                                System.out.println(jr.getBigDecimal());
//                                //System.out.println("Adding " + jr.getBigDecimal());
//                                arr.add(jr.getBigDecimal().doubleValue());
//                            }
//                            break;
//                        }
//                        case VALUE_STRING: {
//                            System.out.println(jr.getString());
//                            break;
//                        }
//                        case VALUE_TRUE: {
//                            System.out.println(true);
//                            break;
//                        }
//                        case START_OBJECT: {
//                            System.out.println("{");
//                            break;
//                        }
//                        case END_OBJECT: {
//                            System.out.println("}");
//                            break;
//                        }
//                        default: {
//
//                        }
//                    }
//                    event = jr.next();
//                }
//                Double[] d = new Double[arr.size()];
//                arr.toArray(d);
//                mes.value = ArrayUtils.toPrimitive(d);
//
//            }
           
            // Send the message to the handler of the app and get the response message
            // This has to be blocking = no thread or use join()
            Message response = app.messageHandler(mes);
            // set the response code and write the response data
            try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
                Map<String, Object> properties = new HashMap<>(1);
                properties.put(JsonGenerator.PRETTY_PRINTING, true);
                JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
                try (JsonGenerator jg = jgf.createGenerator(writer)) {
                    double dbl = 0.01d;
                    jg.writeStartObject();
                    jg.write("type", response.type);
                    jg.writeStartArray("values");
                    for (int i = 0; i < response.value.length; i++) {
                        jg.writeStartObject();
                        jg.write("value", response.value[i]);
                        jg.writeEnd();
                    }
                    jg.writeEnd();
                    jg.writeEnd();
                }
            }
        } catch (IOException e) {
        }

    }

}
