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
package es.gpc.server;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import es.gpc.generic.GPCApp;
import es.gpc.utils.GlobalLog;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author marc
 */
public class DefaultServlet extends HttpServlet {

    private String greeting = "This is AMB-ML framework's default page.";
    GlobalLog glog = null;

    public DefaultServlet() {
    }

    public DefaultServlet(String greeting) {
        this.greeting = greeting;
    }

    public DefaultServlet(GlobalLog glob) {
        glog = glob;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<head><meta http-equiv=\"refresh\" content=\"10\"></head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>" + greeting + "</h1>");
        response.getWriter().println("<h2>Current status: "+glog.state+"</h2>");
        try {
            List<String> log = Files.readAllLines(new File("out.stat").toPath());
            if (log.isEmpty()) {
                response.getWriter().println("<p>Ready to rock!!</p>");
            } else {
                response.getWriter().println("<p>");
                for (String s : log) {
                    response.getWriter().println(s + "<br>");
                }
                response.getWriter().println("</p>");
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        response.getWriter().println("session=" + request.getSession(true).getId());
        response.getWriter().println("</body>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        //here we have to test the type of messages we receive
        try {
            int len = req.getContentLength();
            byte[] input = new byte[len];
            try (ServletInputStream sin = req.getInputStream()) {
                JsonParser jr = Json.createParser(sin);
                JsonParser.Event event;

                String inString = new String(input);
                System.out.println(inString);
                event = jr.next();

                // Output contents of "address" object
                while (event != JsonParser.Event.END_OBJECT) {
                    switch (event) {
                        case KEY_NAME: {

                            System.out.print(jr.getString());
                            System.out.print(" = ");
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
                                System.out.println(jr.getBigDecimal());
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
                        default: {
                        }
                    }
                    event = jr.next();
                }
            }

            // set the response code and write the response data
            resp.setStatus(HttpServletResponse.SC_OK);
            try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream())) {
                writer.write("answer");
                writer.flush();
            }
        } catch (IOException e) {
            try {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().print(e.getMessage());
                resp.getWriter().close();
            } catch (IOException ioe) {
                System.out.println(ioe.toString());
            }
        }

    }
}
