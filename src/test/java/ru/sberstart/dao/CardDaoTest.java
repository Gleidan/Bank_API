package ru.sberstart.dao;

import ru.sberstart.dao.CardDao;
import ru.sberstart.model.Account;
import ru.sberstart.model.Card;
import ru.sberstart.model.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CardDaoTest {

    private EmbeddedDatabase db;
    private CardDao cardDao;

    @BeforeEach
    void init() throws SQLException {
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .build();
        cardDao = new CardDao(db.getConnection());
    }

    @Test
    void getByIdTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 100.12);");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        Account owner_account = new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner);
        Card expected = new Card(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9010"), owner_account, 100.12);
        Card actual = cardDao.getById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void saveTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        Account owner_account = new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner);
        Card expected = new Card(null, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9010"), owner_account, 100.12);
        cardDao.save(expected);
        expected.setId(1L);
        assertEquals(expected, cardDao.getById(1L));
    }

    @Test
    void updateTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 100.12);");
        connection.close();
        Card expected = cardDao.getById(1L);
        expected.setNumber(UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9021"));
        assertNotEquals(expected, cardDao.getById(1L));
        cardDao.update(expected);
        assertEquals(expected, cardDao.getById(1L));
    }

    @Test
    void getAllTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 100.12);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9011', 1, 315.21);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9012', 1, 12312.32131);");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        Account owner_account = new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner);
        List<Card> expectedList = new LinkedList() {{
            add(new Card(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9010"), owner_account, 100.12));
            add(new Card(2L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9011"), owner_account, 315.21));
            add(new Card(3L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9012"), owner_account, 12312.32131));
        }};
        List<Card> actual = cardDao.getAll();
        assertEquals(expectedList, actual);
    }

    @Test
    void deleteTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 100.12);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9011', 1, 315.21);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9012', 1, 12312.32131);");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        Account owner_account = new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner);
        List<Card> expectedList = new LinkedList() {{
            add(new Card(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9010"), owner_account, 100.12));
            add(new Card(2L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9011"), owner_account, 315.21));
            add(new Card(3L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9012"), owner_account, 12312.32131));
        }};
        List<Card> actual = cardDao.getAll();
        assertEquals(expectedList, actual);
        expectedList.remove(1);
        cardDao.delete(2L);
        actual = cardDao.getAll();
        assertEquals(expectedList, actual);
    }

    @AfterEach
    void shutdown() throws SQLException {
        db.shutdown();
    }
}
