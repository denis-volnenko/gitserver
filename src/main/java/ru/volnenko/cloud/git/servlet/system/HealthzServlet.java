package ru.volnenko.cloud.git.servlet.system;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class HealthzServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Content-Type","text/plain; charset=utf-8");
        resp.getWriter().println("OK");
    }

}
