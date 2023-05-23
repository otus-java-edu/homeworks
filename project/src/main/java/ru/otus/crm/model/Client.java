package ru.otus.crm.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.*;
import java.util.stream.Collectors;

@Table("client")
public class Client implements Cloneable, Persistable<Long> {

    @Id
    private Long id;

    private String name;

    @Nonnull
    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    @Transient
    private boolean isNew = true;
    public Client() {
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = new Address(address);
    }

    public void setPhones(List<String> phones) {
        this.phones = new HashSet<>();
        phones.forEach(p->this.phones.add(new Phone(null, p)));
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
        this.phones = new HashSet<>();
        this.address = new Address("");
    }
    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
        this.isNew = false;
    }

    @Override
    public Client clone() {
        var client = new Client(this.id, this.name);
        if (this.address != null)
            client.address = this.address.clone();
        if (this.phones != null)
            client.phones = this.phones.stream().map(p->{var n = p.clone(); n.setClient(client); return n;}).collect(Collectors.toSet());
        client.isNew = this.isNew;
        return client;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phones='" + String.join(";", phones.stream().map(p->p.getPhone()).toList()) + '\'' +
                '}';
    }

    public Address getAddress() {
        return address;
    }

    public Collection<Phone> getPhones() {
        return phones;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
