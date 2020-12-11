package ru.sberstart.dao;

import ru.sberstart.dao.ClientDao;
import ru.sberstart.model.Client;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class ClientDaoTest {

    private EmbeddedDatabase db;
    private ClientDao daoClient;

    @BeforeEach
    void init() throws SQLException {
        db = new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .build();
        daoClient = new ClientDao(db.getConnection());
    }

    @Test
    void getByIdTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Ivan','Ivanov');");
        connection.close();
        Client expected = new Client(1L, "Ivan", "Ivanov");
        Client actual = daoClient.getById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void saveTest() throws SQLException {
        Client client = new Client(null, "Ivan", "Ivanov");
        daoClient.save(client);
        client.setId(1L);
        assertEquals(client, daoClient.getById(1L));
    }

    @Test
    void updateTest() throws SQLException {
        Client modif = new Client(null, "Ivanov", "Ivanov");
        Client expected;
        daoClient.save(modif);
        modif.setId(1L);
        expected = daoClient.getById(1L);
        assertEquals(modif, expected);
        modif.setName("Petrov");
        modif.setSurname("Petrov");
        daoClient.update(modif);
        expected = daoClient.getById(1L);
        assertEquals(modif, expected);
    }

    @Test
    void getAllTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Ivan','Ivanov');");
        connection.close();
        List<Client> expectedList = new LinkedList() {{
            add(new Client(1L, "Petr", "Petrov"));
            add(new Client(2L, "Ivan", "Ivanov"));
        }};
        List<Client> actual = daoClient.getAll();
        assertEquals(expectedList, actual);
    }

    @Test
    void deleteTest() throws SQLException {
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Ivan','Ivanov');");
        statement.execute("INSERT INTO client(id, name, surname) VALUES(DEFAULT,'Semen','Semenov');");
        connection.close();
        List<Client> expectedList = new LinkedList() {{
            add(new Client(1L, "Petr", "Petrov"));
            add(new Client(2L, "Ivan", "Ivanov"));
            add(new Client(3L, "Semen", "Semenov"));
        }};
        List<Client> actual = daoClient.getAll();
        assertEquals(expectedList, actual);
        expectedList.remove(1);
        daoClient.delete(2L);
        actual = daoClient.getAll();
        assertEquals(expectedList, actual);
    }

    @AfterEach
    void shutdown() throws SQLException {
        db.shutdown();
    }
}
