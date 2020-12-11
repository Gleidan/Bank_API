package ru.sberstart.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import ru.sberstart.dao.CardDao;
import ru.sberstart.model.Account;
import ru.sberstart.model.Card;
import ru.sberstart.server.CreateDataSource;
import ru.sberstart.server.ServerRunner;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CardHandlersTest {

    private final String cardUrl = "http://localhost:8081/";
    private ServerRunner serverRunner;
    private CardDao cardDao;

    @BeforeEach
    public void init() throws IOException {
        serverRunner = new ServerRunner();
        serverRunner.start();
        try {
            cardDao = new CardDao(CreateDataSource.getDataSource().getConnection());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void checkBalanceHandlerTest() {
        StringBuilder textBuilder = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(cardUrl + "checkBalance?id=1").openConnection();
            try (InputStream inputStream = connection.getInputStream()) {
                try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8.name()))) {
                    int c = 0;
                    while ((c = reader.read()) != -1) {
                        textBuilder.append((char) c);
                    }
                }
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("999.99", textBuilder.toString());
    }

    @Test
    public void getAllClientCardsHandlerTest() throws SQLException {
        RestAssured.defaultParser = Parser.JSON;
        Card[] card = given().when().get(cardUrl + "getAllClientCards?id=1").as(Card[].class);
        List<Card> cardList = cardDao.getAllClientCard(1L);
        Card[] expected = new Card[cardList.size()];
        expected = cardDao.getAllClientCard(1L).toArray(expected);

        assertTrue(Arrays.equals(expected, card));
    }

    @Test
    public void CreateCardHandlerTest() throws IOException, SQLException {
        List<Card> cardListOfAccount = cardDao.getAllCardOnAccount(2L);

        assertTrue(cardListOfAccount.size() == 1);

        HttpURLConnection connection = (HttpURLConnection) new URL(cardUrl + "createCard").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        Account account = new Account(2L, null, null);

        OutputStream outputStream = connection.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String str = writer.writeValueAsString(account);

        outputStream.write(str.getBytes());
        outputStream.flush();
        outputStream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = reader.readLine()) != null) {
            response.append(responseLine.trim());
        }

        assertEquals("\"Created\"", response.toString());
        cardListOfAccount = cardDao.getAllCardOnAccount(2L);
        assertTrue(cardListOfAccount.size() == 2);
    }

    @Test
    public void ChangeBalanceHandlerTest() throws SQLException, IOException {
        Card expected = cardDao.getById(1L);
        Double sum = -177.10;
        expected.changeBalance(sum);

        HttpURLConnection connection = (HttpURLConnection) new URL(cardUrl + "changeBalance").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setDoOutput(true);

        Card card = new Card(1L, null, null, -177.10);

        OutputStream outputStream = connection.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String str = writer.writeValueAsString(card);

        outputStream.write(str.getBytes());
        outputStream.flush();
        outputStream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = reader.readLine()) != null) {
            response.append(responseLine.trim());
        }

        assertEquals("\"Changed\"", response.toString());
        Card actual = cardDao.getById(1L);
        assertEquals(expected, actual);
    }

    @AfterEach
    public void shutdownServer() {
        serverRunner.stop();
    }
}
