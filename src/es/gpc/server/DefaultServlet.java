/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.gpc.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.servlet.ServletInputStream;

import es.gpc.generic.AMBApp;

 

/**
 *
 * @author marc
 */
public class DefaultServlet extends HttpServlet
{
    private String greeting="This is AMB-ML framework's default page.";
    private final AMBApp app = null;
    public DefaultServlet(){}
    public DefaultServlet(String greeting)
    {
        this.greeting=greeting;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>"+greeting+"</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
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
            }
        }

    }
}
