package ru.volnenko.cloud.git;

import lombok.NonNull;
import ru.volnenko.cloud.git.component.GitServer;

public final class Application {

    public static void main(final String[] args) {
        @NonNull final GitServer gitServer = new GitServer();
        gitServer.start();
    }

}
