package ru.sberstart.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.tools.RunScript;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.sberstart.dao.CardDao;
import ru.sberstart.model.Card;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CardServiceImplTest {

    private static DataSource dataSource;
    private CardDao cardDao;
    private CardServiceImpl cardService;

    @BeforeEach
    void init() throws SQLException {
        HikariConfig config = new HikariConfig();
        HikariDataSource ds;

        config.setJdbcUrl("jdbc:h2:~/test");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

        try {
            RunScript.execute(ds.getConnection(), new FileReader("src/test/resources/schema.sql"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }

        dataSource = ds;
        cardDao = new CardDao(ds.getConnection());
        cardService = new CardServiceImpl(ds);
    }

    @Test
    public void checkBalanceTest() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 100.12);");
        connection.close();

        Double expected = cardDao.getById(1L).getBalance();
        Double actual = cardService.checkBalance(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void changeBalanceTest() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 100.12);");
        connection.close();

        Card card = cardDao.getById(1L);
        Double sum = -30.0;
        card.changeBalance(sum);
        cardService.changeBalance(1L, sum);
        assertEquals(card, cardDao.getById(1L));
    }

    @Test
    public void getAllClientCardTest() throws SQLException {
        Connection connection = dataSource.getConnection();
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

        List<Card> expected = cardDao.getAllClientCard(1L);
        List<Card> actual = cardService.getAllClientCard(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void getAllCardOnAccountTest() throws SQLException {
        Connection connection = dataSource.getConnection();
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

        List<Card> expected = cardDao.getAllCardOnAccount(1L);
        List<Card> actual = cardService.getAllCardOnAccount(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void createNewCardTest() throws SQLException {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        statement.execute("INSERT INTO client(id, name, surname) " +
                "VALUES(DEFAULT,'Petr','Petrov');");
        statement.execute("INSERT INTO account(id, number, client_id) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9000', 1);");
        statement.execute("INSERT INTO card(id, number, account_id, balance) " +
                "VALUES(DEFAULT,'43da9b65-2184-469c-b9e4-f7d3cfbf9010', 1, 100.12);");
        connection.close();

        List<Card> expected = cardDao.getAllCardOnAccount(1L);
        cardService.createNewCard(1L);
        List<Card> actual = cardDao.getAllCardOnAccount(1L);
        assertTrue(expected.size() < actual.size());
    }
}
