package ru.sberstart.dao;

import ru.sberstart.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/*
annp
 */

public class CardDao extends AbstractDao<Card> {

    public CardDao() {
    }

    public CardDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM card";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO card(id, number, account_id, balance) VALUES(DEFAULT, ?, ?, ?);";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM card WHERE id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE card SET number = ?, account_id = ?, balance = ? WHERE id = ?;";
    }

    @Override
    protected List<Card> parseResultTest(ResultSet rs) {
        List<Card> cardList = new LinkedList<Card>();
        AccountDao accountDao = new AccountDao(connection);

        try {
            while (rs.next()) {
                Card tempCard = new Card();
                tempCard.setId(rs.getLong("id"));
                tempCard.setNumber(UUID.fromString(rs.getString("number")));
                tempCard.setBalance(rs.getDouble("balance"));
                tempCard.setAccount(accountDao.getById(rs.getLong("account_id")));
                cardList.add(tempCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardList;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Card card) {
        try {
            statement.setString(1, card.getNumber().toString());
            statement.setLong(2, card.getAccount().getId());
            statement.setDouble(3, card.getBalance());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Card card) {
        try {
            statement.setString(1, card.getNumber().toString());
            statement.setLong(2, card.getAccount().getId());
            statement.setDouble(3, card.getBalance());
            statement.setLong(4, card.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Card> getAllClientCard(Long client_id) throws SQLException {
        List<Card> list;
        String sql = "SELECT * FROM CARD INNER JOIN ACCOUNT A on CARD.ACCOUNT_ID = A.ID WHERE A.CLIENT_ID = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, client_id);
            try (ResultSet rs = statement.executeQuery()) {
                list = parseResultTest(rs);
            }
        }
        return list;
    }

    public List<Card> getAllCardOnAccount(Long account_id) throws SQLException {
        List<Card> list;
        String sql = "SELECT * FROM CARD INNER JOIN ACCOUNT A on CARD.ACCOUNT_ID = A.ID WHERE ACCOUNT_ID = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, account_id);
            try (ResultSet rs = statement.executeQuery()) {
                list = parseResultTest(rs);
            }
        }
        return list;
    }
}
