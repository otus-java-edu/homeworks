package ru.otus.jdbc.impl;

import ru.otus.core.repository.DataTemplateException;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.orm.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl implements EntityClassMetaData {
    private final Class clazz;
    private final String name;

    private final Constructor constructor;

    private final List<Field> fields;
    private final List<Field> fieldsWithoutId;

    private final Field idField;
    public EntityClassMetaDataImpl(Class clazz) {
        this.clazz = clazz;
        this.name = clazz.getSimpleName().toLowerCase();
        try {
            constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException(e);
        }
        fields = Arrays.stream(clazz.getDeclaredFields()).toList();
        idField = fields.stream().filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get();
        fieldsWithoutId = fields.stream().filter(f->!f.isAnnotationPresent(Id.class)).toList();
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields.stream().toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId.stream().toList();
    }
}
