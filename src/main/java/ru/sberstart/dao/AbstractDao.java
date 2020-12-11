package ru.sberstart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao<T> implements GenericDao<T> {

    protected Connection connection;

    protected abstract String getSelectQuery();

    protected abstract String getInsertQuery();

    protected abstract String getDeleteQuery();

    protected abstract String getUpdateQuery();

    protected abstract List<T> parseResultTest(ResultSet rs);

    protected abstract void prepareStatementForInsert(PreparedStatement statement, T entity);

    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T entity);

    public AbstractDao() {
    }

    public AbstractDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public T getById(Long id) throws SQLException {
        List<T> list;
        String  sql = getSelectQuery();

        sql += " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                list = parseResultTest(rs);
            }
        }
        return list.iterator().next();
    }

    @Override
    public void save(T entity) throws SQLException {
        String sql = getInsertQuery();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, entity);
            statement.executeUpdate();
        }
    }

    @Override
    public void update(T entity) throws SQLException {
        String sql = getUpdateQuery();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(statement, entity);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = getDeleteQuery();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public List<T> getAll() throws SQLException {
        List<T> list;
        String sql = getSelectQuery();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                list = parseResultTest(rs);
            }
        }
        return list;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
