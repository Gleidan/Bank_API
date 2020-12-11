package ru.sberstart;

import ru.sberstart.server.ServerRunner;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new ServerRunner().start();
    }
}
