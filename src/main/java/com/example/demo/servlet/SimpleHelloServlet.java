package com.example.demo.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/hello-servlet")
public class SimpleHelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {

        resp.setContentType("text/plain");
        resp.getWriter().write("Hello from servlet!");
    }
}
