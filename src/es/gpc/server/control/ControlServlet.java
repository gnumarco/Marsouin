/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.gpc.server.control;

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

/**
 *
 * @author Marc Segond
 */
public class ControlServlet extends HttpServlet {

    private String greeting = "Hello World";
    private final es.gpc.generic.AMBApp app;

    public ControlServlet() {
        //app = new SimpleControlServer();
        app = new GPControlServer();
    }

    public ControlServlet(String greeting) {
        this.greeting = greeting;
        //app = new SimpleControlServer();
        app = new GPControlServer();
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
        int len = req.getContentLength();
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
                    System.out.println("value : " + result.getJsonNumber("value"));
                    d[i] = result.getJsonNumber("value").doubleValue();
                }
                mes.value = d;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            // Send the message to the handler of the app and get the response message
            // This has to be blocking = no thread or use join()
            Message response = app.messageHandler(mes);
            // set the response code and write the response data
            try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream())) {
                Map<String, Object> properties = new HashMap<>(1);
                properties.put(JsonGenerator.PRETTY_PRINTING, true);
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
