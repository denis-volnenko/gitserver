package ru.volnenko.cloud.git.servlet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

import java.io.IOException;

public final class IndexFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        @NonNull final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        @NonNull final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (httpServletRequest.getRequestURI().equals("/")) {
            httpServletResponse.addHeader("Content-Type","text/plain; charset=utf-8");
            httpServletResponse.getWriter().println("GITSERVER");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
