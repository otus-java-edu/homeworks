package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public class Address {

    @Id
    private Long clientId;

    private final String address;
    public Address(String address) {
        this.address = address;
    }

    @PersistenceCreator
    public Address(Long clientId, String address) {
        this.clientId = clientId;
        this.address = address;
    }

    @Override
    public Address clone() {
        return new Address(this.clientId, this.address);
    }

    @Override
    public String toString()
    {
        return address;
    }
}
