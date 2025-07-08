package ru.volnenko.cloud.git.servlet.repository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import ru.volnenko.cloud.git.component.RepositoryInitializer;

import java.io.IOException;

public final class RepositoryDescriptionServlet extends HttpServlet {

    @NonNull
    private final RepositoryInitializer repositoryInitializer;

    public RepositoryDescriptionServlet(@NonNull final RepositoryInitializer repositoryInitializer) {
        this.repositoryInitializer = repositoryInitializer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("123");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

}
