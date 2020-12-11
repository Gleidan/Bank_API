package ru.sberstart.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberstart.model.Card;
import ru.sberstart.service.CardServiceImpl;

import java.io.IOException;
import java.io.InputStream;

public class ChangeBalanceHandler implements HttpHandler {

    private final CardServiceImpl service;

    public ChangeBalanceHandler(CardServiceImpl service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Long id = null;
        Double sum = null;

        if ("POST".equals(exchange.getRequestMethod())) {
            Card card = requestCard(exchange);
            id = card.getId();
            sum = card.getBalance();
        }
        service.changeBalance(id, sum);
        new HandlerUtility<String>().response("Changed", exchange);
    }

    public Card requestCard(HttpExchange exchange) {
        InputStream inputStream = exchange.getRequestBody();
        ObjectMapper objectMapper = new ObjectMapper();
        Card card = null;

        try {
            card = objectMapper.readValue(inputStream, Card.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return card;
    }
}
