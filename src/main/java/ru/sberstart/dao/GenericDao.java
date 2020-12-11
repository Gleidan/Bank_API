package ru.sberstart.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDao<T> {

    public T getById(Long id) throws SQLException;

    public void save(T entity) throws SQLException;

    public void update(T entity) throws SQLException;

    public void delete(Long id) throws SQLException;

    public List<T> getAll() throws SQLException;
}
