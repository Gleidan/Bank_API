package ru.sberstart.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberstart.model.Account;
import ru.sberstart.model.Card;
import ru.sberstart.service.CardServiceImpl;

import java.io.IOException;
import java.io.InputStream;

public class CreateCardHandler implements HttpHandler {

    private final CardServiceImpl service;

    public CreateCardHandler(CardServiceImpl service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Long account_id = null;
        Account account;

        if ("POST".equals(exchange.getRequestMethod())) {
            account = requestAccountId(exchange);
            account_id = account.getId();
        }

        service.createNewCard(account_id);
        new HandlerUtility<String>().response("Created", exchange);
    }

    public Account requestAccountId(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        ObjectMapper objectMapper = new ObjectMapper();
        Account account = null;

        try {
            account = objectMapper.readValue(inputStream, Account.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return account;
    }
}
