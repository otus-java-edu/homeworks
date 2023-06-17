package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Table("symbols")
@Getter
public class Symbol {
    @Id
    private final Integer id;
    private final String name;
}
