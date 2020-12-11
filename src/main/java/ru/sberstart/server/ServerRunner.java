package ru.sberstart.server;

import com.sun.net.httpserver.HttpServer;
import ru.sberstart.server.handler.ChangeBalanceHandler;
import ru.sberstart.server.handler.CreateCardHandler;
import ru.sberstart.server.handler.GetAllClientCardsHandler;
import ru.sberstart.server.handler.CheckBalanceHandler;
import ru.sberstart.service.CardServiceImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerRunner {

    private final DataSource ds = CreateDataSource.createDataSource();
    private final CardServiceImpl service = new CardServiceImpl(ds);
    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 8081), 0);

        server.createContext("/checkBalance", new CheckBalanceHandler(service));
        server.createContext("/changeBalance", new ChangeBalanceHandler(service));
        server.createContext("/getAllClientCards", new GetAllClientCardsHandler(service));
        server.createContext("/createCard", new CreateCardHandler(service));

        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
