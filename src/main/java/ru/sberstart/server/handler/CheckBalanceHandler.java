package ru.sberstart.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberstart.service.CardServiceImpl;

import java.io.IOException;

public class CheckBalanceHandler implements HttpHandler {

    private final CardServiceImpl service;

    public CheckBalanceHandler(CardServiceImpl service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Long cardId = null;
        Double balance;

        if ("GET".equals(exchange.getRequestMethod())) {
            cardId = HandlerUtility.requestParam(exchange);
        }

        balance = service.checkBalance(cardId);
        new HandlerUtility<Double>().response(balance, exchange);
    }
}
