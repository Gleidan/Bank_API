package ru.sberstart.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class HandlerUtility<T> {

    public void response(T entity, HttpExchange exchange) {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            String str = writer.writeValueAsString(entity);

            exchange.sendResponseHeaders(200, str.length());

            outputStream.write(str.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Long requestParam(HttpExchange exchange) {
        return Long.parseLong(exchange
                .getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1]);
    }
}
