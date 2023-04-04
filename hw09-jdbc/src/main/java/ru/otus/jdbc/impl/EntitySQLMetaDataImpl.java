package ru.otus.jdbc.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData metaData;
    private final String insertTokens;

    public EntitySQLMetaDataImpl(EntityClassMetaData metaData){

        this.metaData = metaData;
        insertTokens = String.join(",", Collections.nCopies(metaData.getFieldsWithoutId().size(), "?"));
    };

    @Override
    public String getSelectAllSql() {
        return String.format("select %s from %s", String.join(",",
                getFieldsNames(metaData.getAllFields())), metaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select %s from %s where %s=?", String.join(",",
                getFieldsNames(metaData.getFieldsWithoutId())), metaData.getName(), metaData.getIdField().getName().toLowerCase());
    }

    @Override
    public String getInsertSql() {
        return String.format("insert into %s(%s) values(%s)", metaData.getName(),
                String.join(",", getFieldsNames(metaData.getFieldsWithoutId())), insertTokens);
    }

    @Override
    public String getUpdateSql() {
        return String.format("update %s set %s=? where %s=?", metaData.getName(), String.join("=?,",
                getFieldsNames(metaData.getFieldsWithoutId())), metaData.getIdField().getName().toLowerCase());
    }

    private List<String> getFieldsNames(List<Field> fields){
        return fields.stream().map(f->f.getName().toLowerCase()).collect(Collectors.toUnmodifiableList());
    }
}
