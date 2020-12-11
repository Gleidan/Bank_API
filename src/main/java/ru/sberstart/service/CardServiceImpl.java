package ru.sberstart.service;

import ru.sberstart.dao.AccountDao;
import ru.sberstart.dao.CardDao;
import ru.sberstart.model.Account;
import ru.sberstart.model.Card;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CardServiceImpl implements CardService {

    private final DataSource dataSource;
    private final CardDao cardDao;

    public CardServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        cardDao = new CardDao();
    }

    @Override
    public Double checkBalance(Long id) {
        Card card;
        Double balance = null;

        try (Connection connection = dataSource.getConnection()) {
            cardDao.setConnection(connection);
            card = cardDao.getById(id);
            balance = card.getBalance();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    @Override
    public boolean changeBalance(Long id, Double sum) {
        Card card;

        try (Connection connection = dataSource.getConnection()) {
            cardDao.setConnection(connection);
            card = cardDao.getById(id);
            card.changeBalance(sum);
            cardDao.update(card);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public List<Card> getAllClientCard(Long client_id) {
        List<Card> cardList = null;

        try (Connection connection = dataSource.getConnection()) {
            cardDao.setConnection(connection);
            cardList = cardDao.getAllClientCard(client_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardList;
    }

    @Override
    public List<Card> getAllCardOnAccount(Long account_id) {
        List<Card> cardList = null;

        try (Connection connection = dataSource.getConnection()) {
            cardDao.setConnection(connection);
            cardList = cardDao.getAllCardOnAccount(account_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardList;
    }

    @Override
    public boolean createNewCard(Long account_id) {
        Card card;
        Account account = null;

        try (Connection accConnection = dataSource.getConnection()) {
            AccountDao accountDao = new AccountDao(accConnection);
            account = accountDao.getById(account_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        card = new Card(null, UUID.randomUUID(), account, 0.0);

        try (Connection connection = dataSource.getConnection()) {
            cardDao.setConnection(connection);
            cardDao.save(card);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
