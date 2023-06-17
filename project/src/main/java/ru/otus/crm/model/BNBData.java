package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Table("bnb_data")
@Getter
public class BNBData {
    @Id
    private final Long time;
    private final Float price;
}
