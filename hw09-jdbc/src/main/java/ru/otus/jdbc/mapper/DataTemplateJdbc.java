package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final Constructor classConstructor;
    private final List<Field> allClassFields;
    private final Field idField;
    private final List<Field> classFieldsWithoutId;
    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        classConstructor = entityClassMetaData.getConstructor();
        classConstructor.setAccessible(true);
        allClassFields = entityClassMetaData.getAllFields();
        classFieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        for (var field : allClassFields){
            field.setAccessible(true);
        }
        for (var field : classFieldsWithoutId){
            field.setAccessible(true);
        }
        idField = entityClassMetaData.getIdField();
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    var result = classConstructor.newInstance();
                    for (var field : classFieldsWithoutId){
                        try {
                            field.set(result, rs.getObject(rs.findColumn(field.getName().toLowerCase())));
                        }catch(Exception e){
                            throw new DataTemplateException(e);
                        }
                    }
                    return (T) result;
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), rs -> {
            try {
                var result = new ArrayList<T>();
                if (rs.next()) {
                    var res = classConstructor.newInstance();
                    for (var field : allClassFields){
                        try {
                            field.set(result, rs.getObject(rs.findColumn(field.getName().toLowerCase())));
                        }catch(Exception e){
                            throw new DataTemplateException(e);
                        }
                    }
                    result.add((T) res);
                }
                return result;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).get();
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            List<Object> values = classFieldsWithoutId.stream()
                    .map(f-> {
                        try {
                            var v = f.get(client);
                            if (v != null)
                                return v.toString();
                            else
                                return "null";
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(),
                    values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            List<Object> values = classFieldsWithoutId.stream().map(f-> {
                try {
                    var v = f.get(client);
                    if (v != null)
                        return v.toString();
                    else
                        return "null";
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            values.add(idField.get(client).toString());
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(),
                    values);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
