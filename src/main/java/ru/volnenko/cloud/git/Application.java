package ru.volnenko.cloud.git;

import ru.volnenko.cloud.git.component.GitServer;

public class Application {

    public static void main(String[] args) {
        final GitServer gitServer = new GitServer();

        gitServer.start();
    }

}
