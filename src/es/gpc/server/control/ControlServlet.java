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

import es.gpc.utils.GlobalLog;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.json.Json;
import javax.json.*;

import es.gpc.utils.Message;

import java.util.HashMap;
import java.util.Map;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc Segond
 */
public class ControlServlet extends HttpServlet {

    private final String greeting = "Hello World";
    private final es.gpc.generic.GPCApp app;
    GlobalLog glog = null;

//    public ControlServlet() {
//        app = new GPControlServer();
//    }
//
//    public ControlServlet(String greeting) {
//        this.greeting = greeting;
//        app = new GPControlServer();
//    }
    
    public ControlServlet(GlobalLog glob) {
        glog = glob;
        app = new GPControlServer(glog);
        if(glog == null)
            System.err.println("PROB!!");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>" + greeting + "</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Message mes = new Message();
        try {
            InputStream is = req.getInputStream();

            JsonReader rdr = Json.createReader(is);

            JsonObject obj = rdr.readObject();
            System.out.println("Starting reading Json message");
            String t = obj.getString("type");
            mes.type = obj.getString("type");
            JsonArray values = obj.getJsonArray("values");
            JsonObject result;
            if (t.equalsIgnoreCase("start")) {

            } else if (t.equalsIgnoreCase("config")) {
                mes.config = new HashMap<>();
                for (int i = 0; i < values.size(); i++) {
                    result = values.getJsonObject(i);
                    System.out.println("value : " + result.getJsonString("param").getString());
                    System.out.println("value : " + result.getJsonString("value").getString());
                    mes.config.put(result.getJsonString("param").getString(), result.getJsonString("value").getString());
                }
            } else {
                double[] d = new double[values.size()];
                System.out.println("type : " + t);
                for (int i = 0; i < values.size(); i++) {
                    result = values.getJsonObject(i);
                    System.out.println("value : " + result.getJsonString("value"));
                    d[i] = Double.valueOf(result.getJsonString("value").getString());
                }
                mes.value = d;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            // Send the message to the handler of the app and get the response message
            // This has to be blocking = no thread or use join()
            Message response;
            if (((GPControlServer) app).e.isAlive()) {
                response = app.messageHandler(mes);
                try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream())) {
                    Map<String, Object> properties = new HashMap<>(1);
                    //properties.put(JsonGenerator.PRETTY_PRINTING, true);
                    JsonGeneratorFactory jgf = Json.createGeneratorFactory(properties);
                    try (JsonGenerator jg = jgf.createGenerator(writer)) {
                        jg.writeStartObject();
                        jg.write("type", response.type);
                        if (!response.type.equalsIgnoreCase("info")) {
                            jg.writeStartArray("values");
                            for (int i = 0; i < response.value.length; i++) {
                                jg.writeStartObject();
                                jg.write("value", response.value[i]);
                                jg.writeEnd();
                            }
                            jg.writeEnd();
                        }
                        jg.writeEnd();
                    }
                }
            } else {
                if (mes.type.equalsIgnoreCase("config") || mes.type.equalsIgnoreCase("start")) {
                    response = app.messageHandler(mes);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println(response.type);
                    resp.getWriter().close();
                } else {
                    try {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().println("No Living instance of the GP-Control kernel");
                        resp.getWriter().close();
                    } catch (IOException ex) {
                        Logger.getLogger(ControlServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // set the response code and write the response data
        } catch (IOException e) {
            try {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(e.getMessage());
                resp.getWriter().close();
            } catch (IOException ioe) {
            }
        }

    }

}
