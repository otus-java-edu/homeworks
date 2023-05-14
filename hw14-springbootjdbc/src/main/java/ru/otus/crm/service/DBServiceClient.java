package ru.otus.crm.service;

import ru.otus.crm.model.Client;

import java.util.Optional;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    Iterable<Client> findAll();
}
