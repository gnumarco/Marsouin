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



import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.Json;
import javax.json.*;

import es.gpc.utils.Message;

import java.util.HashMap;
import java.util.Map;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author marc
 */
public class ControlSocketHandler extends Thread {

    private final es.gpc.generic.GPCApp app;
    private InputStream is;
    private OutputStream out;

    public ControlSocketHandler(InputStream i, OutputStream o) {
        app = new SimpleControlServer();
        is = i;
        out = o;
    }

    public ControlSocketHandler(String greeting) {
        app = new SimpleControlServer();
    }

    @Override
    public void run() {
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

            // Send the message to the handler of the app and get the response message
            // This has to be blocking = no thread or use join()
            Message response = app.messageHandler(mes);
            // set the response code and write the response data
            try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
                Map<String, Object> properties = new HashMap<>(1);
                properties.put(JsonGenerator.PRETTY_PRINTING, true);
                JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
                try (JsonGenerator jg = jgf.createGenerator(writer)) {
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
            System.out.println(e.toString());
        }

    }

}
