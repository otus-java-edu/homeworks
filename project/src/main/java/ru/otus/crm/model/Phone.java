package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public class Phone {
    @Id
    private final Long id;

    private final String phone;

    private Long clientId;

    public Phone (Long id, String phone){
        this.id = id;
        this.phone = phone;
    }
    @PersistenceCreator
    private Phone (Long id, String phone, Long clientId){
        this.id = id;
        this.phone = phone;
        this.clientId = clientId;
    }
    @Override
    public Phone clone() {
        return new Phone(this.id, this.phone, this.clientId);
    }

    public void setClient(Client client){
        this.clientId = client.getId();
    }

    public String getPhone() {
        return phone;
    }
    @Override
    public String toString(){
        return phone;
    }
}
