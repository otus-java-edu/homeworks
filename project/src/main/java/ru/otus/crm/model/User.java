package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Table("users")
@Getter
@Setter
public class User {
    @Id
    private final Integer id;
    private final String name;
    private final String apiKey;
    private final String secretKey;
    private boolean initialized;
}
