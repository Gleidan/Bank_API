package ru.sberstart.dao;

import ru.sberstart.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AccountDao extends AbstractDao<Account> {

    public AccountDao() {
    }
    public AccountDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM account";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO account(id, number, client_id) VALUES(DEFAULT, ?, ?);";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM account WHERE id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE account SET number = ?, client_id = ? WHERE id = ?;";
    }

    @Override
    protected List<Account> parseResultTest(ResultSet rs) {
        List<Account> accountList = new LinkedList<Account>();
        ClientDao clientDao = new ClientDao(connection);

        try {
            while (rs.next()) {
                Account tempAccount = new Account();
                tempAccount.setId(rs.getLong("id"));
                tempAccount.setClient(clientDao.getById(rs.getLong("client_id")));
                tempAccount.setNumber(UUID.fromString(rs.getString("number")));
                accountList.add(tempAccount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Account account) {
        try {
            statement.setString(1, account.getNumber().toString());
            statement.setLong(2, account.getClient().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Account account) {
        try {
            statement.setString(1, account.getNumber().toString());
            statement.setLong(2, account.getClient().getId());
            statement.setLong(3, account.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
