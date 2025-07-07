package ru.volnenko.cloud.git.servlet.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import ru.volnenko.cloud.git.util.SettingUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.StringTokenizer;

public final class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        @NonNull final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        @NonNull final HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        final String authHeader = httpServletRequest.getHeader("Authorization");
        final String username = SettingUtil.getSecurityUsername();
        final String password = SettingUtil.getSecurityPassword();

        if (authHeader != null) {
            @NonNull final StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                @NonNull final String basic = st.nextToken();

                if (basic.equalsIgnoreCase("Basic")) {
                    try {
                        final String credentials = new String(Base64.getDecoder().decode(st.nextToken()));
                        final int p = credentials.indexOf(":");
                        if (p != -1) {
                            final String _username = credentials.substring(0, p).trim();
                            final String _password = credentials.substring(p + 1).trim();

                            if (!username.equals(_username) || !password.equals(_password)) {
                                unauthorized(httpServletResponse, "Bad credentials");
                            }

                            filterChain.doFilter(servletRequest, servletResponse);
                        } else {
                            unauthorized(httpServletResponse, "Invalid authentication token");
                        }
                    } catch (@NonNull final UnsupportedEncodingException e) {
                        throw new Error("Couldn't retrieve authentication", e);
                    }
                }
            }
        } else {
            unauthorized(httpServletResponse, "error");
        }
    }

    private void unauthorized(@NonNull final HttpServletResponse response, @NonNull final String message) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + "seq" + "\"");
        response.sendError(401, message);
    }

}
