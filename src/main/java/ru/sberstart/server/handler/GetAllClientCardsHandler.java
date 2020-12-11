package ru.sberstart.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sberstart.model.Card;
import ru.sberstart.service.CardServiceImpl;

import java.io.IOException;
import java.util.List;

public class GetAllClientCardsHandler implements HttpHandler {

    private final CardServiceImpl service;

    public GetAllClientCardsHandler(CardServiceImpl service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Long client_id = null;
        List<Card> cardList;

        if ("GET".equals(exchange.getRequestMethod())) {
            client_id = HandlerUtility.requestParam(exchange);
        }

        cardList = service.getAllClientCard(client_id);
        new HandlerUtility<List<Card>>().response(cardList, exchange);
    }
}
