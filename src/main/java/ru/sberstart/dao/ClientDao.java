package ru.sberstart.dao;

import ru.sberstart.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ClientDao extends AbstractDao<Client> {

    public ClientDao() {
    }

    public ClientDao(Connection connection) {
        super(connection);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM CLIENT";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO client(id, name, surname) VALUES(DEFAULT, ?, ?);";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM client WHERE id = ?;";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE client SET name = ?, surname = ? WHERE id = ?;";
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Client client) {
        try {
            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Client client) {
        try {
            statement.setString(1, client.getName());
            statement.setString(2, client.getSurname());
            statement.setLong(3, client.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<Client> parseResultTest(ResultSet rs) {
        LinkedList<Client> result = new LinkedList<Client>();

        try {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("id"));
                client.setName(rs.getString("name"));
                client.setSurname(rs.getString("surname"));
                result.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
