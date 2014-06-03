/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.gpc.server;

/**
 *
 * @author marc
 */

import es.gpc.server.control.ControlServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
 
public class HTTPServer extends AbstractHandler
{
    @Override
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hello World</h1>");
    }
 
    public static void main(String[] args) throws Exception
    {
         Server server = new Server(8080);
 
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
 
        context.addServlet(new ServletHolder(new ControlServlet()),"/control/*");
        context.addServlet(new ServletHolder(new PredictServlet()),"/predict/*");
        context.addServlet(new ServletHolder(new DefaultServlet()),"/*");
 
        server.start();
        server.join();
    }
}   
