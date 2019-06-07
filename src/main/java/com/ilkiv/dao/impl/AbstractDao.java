package com.ilkiv.dao.impl;

import com.ilkiv.dao.GenericDao;
import com.ilkiv.db.DatabaseConnector;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T, ID> implements GenericDao<T, ID> {

    private Class<T> clazz;
    private static final Logger logger = Logger.getLogger(AbstractDao.class);

    public AbstractDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T save(T t) {
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("insert into " + getTableName() + " values (" + getValues(t) + ")");
        } catch (SQLException e) {
            logger.error("Can`t save item");
        }
        return t;
    }

    public T get(ID id) {
        T item = null;
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from " + clazz.getSimpleName().toUpperCase() + " where ID = " + id);
            if (rs.next()) {
                item = getObjectFromResultSet(rs).get();
            }
        } catch (SQLException e) {
            logger.error("Can`t get item");
        }
        return item;
    }

    public T update(T t) {
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {
            Field field = clazz.getDeclaredField("id");
            field.setAccessible(true);
            long id = Long.parseLong(field.get(t).toString());
            statement.executeUpdate("UPDATE " + getTableName() + " SET " + getUpdateFields(t) + " WHERE ID = " + id);
        } catch (SQLException e) {
            logger.error("Can`t update item");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return t;
    }

    public void delete(ID id) {
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("delete from " + getTableName() + " where ID = " + id);
        } catch (SQLException e) {
            logger.error("Can`t delete item");
        }
    }

    public List<T> getAll() {
        List<T> items = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("select * from " + getTableName());
            while (rs.next()) {
                items.add((T) getObjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private Optional<T> getObjectFromResultSet(ResultSet resultSet) {
        try {
            T object = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            Arrays.stream(fields)
                    .peek((field -> field.setAccessible(true)))
                    .forEach((field -> {
                        try {
                            field.set(object, resultSet.getObject(field.getName().toUpperCase()));
                        } catch (IllegalAccessException | SQLException e) {
                            logger.error("Can`t set field", e);
                        }
                    }));
            return Optional.ofNullable(object);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Can`t get object from ResultSet", e);
        }
        return Optional.empty();
    }

    private String getTableName() {
        return clazz.getSimpleName().toUpperCase();
    }

    private String getValues(T object) {
        StringBuilder values = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                values
                        .append("'")
                        .append(field.get(object))
                        .append("',");
            } catch (IllegalAccessException e) {
                logger.error("Error in getting values", e);
            }
        }
        values.deleteCharAt(values.length() - 1);
        return values.toString();
    }

    private String getUpdateFields(T object) {
        StringBuilder updateFields = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .peek((field -> field.setAccessible(true)))
                .forEach(field -> {
                    try {
                        updateFields
                                .append(field.getName().toUpperCase())
                                .append(" = '")
                                .append(field.get(object))
                                .append("',");
                    } catch (IllegalAccessException e) {
                        logger.error("Error in getting update fields", e);
                    }
                });
        updateFields.deleteCharAt(updateFields.length() - 1);
        return updateFields.toString();
    }
}
