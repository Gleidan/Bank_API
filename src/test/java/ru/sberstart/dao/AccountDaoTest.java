package ru.sberstart.dao;

import ru.sberstart.dao.AccountDao;
import ru.sberstart.model.Account;
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

public class AccountDaoTest {

    private EmbeddedDatabase db;
    private AccountDao accountDao;

    @BeforeEach
    void init() throws SQLException {
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .build();
        accountDao = new AccountDao(db.getConnection());
    }

    @Test
    void getByIdTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        Account expected = new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner);
        Account actual = accountDao.getById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void saveTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        Account expected = new Account(null, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner);
        accountDao.save(expected);
        expected.setId(1L);
        assertEquals(expected, accountDao.getById(1L));
    }

    @Test
    void updateTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        connection.close();
        Account expected = accountDao.getById(1L);
        expected.setNumber(UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9021"));
        assertNotEquals(expected, accountDao.getById(1L));
        accountDao.update(expected);
        assertEquals(expected, accountDao.getById(1L));
    }

    @Test
    void getAllTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9001', 1);");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        Account expected = new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner);
        List<Account> expectedList = new LinkedList() {{
            add(new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner));
            add(new Account(2L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9001"), owner));
        }};
        List<Account> actual = accountDao.getAll();
        assertEquals(expectedList, actual);
    }

    @Test
    void deleteTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9001', 1);");
        statement.execute("INSERT INTO account(id, number, client_id) VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9002', 1);");
        connection.close();
        Client owner = new Client(1L, "Petr", "Petrov");
        List<Account> expectedList = new LinkedList() {{
            add(new Account(1L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9000"), owner));
            add(new Account(2L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9001"), owner));
            add(new Account(3L, UUID.fromString("43da9b65-2184-469c-b9e4-f7d3cfbf9002"), owner));
        }};
        List<Account> actual = accountDao.getAll();
        assertEquals(expectedList, actual);
        expectedList.remove(1);
        accountDao.delete(2L);
        actual = accountDao.getAll();
        assertEquals(expectedList, actual);
    }

    @AfterEach
    void shutdown() throws SQLException {
        db.shutdown();
    }
}
