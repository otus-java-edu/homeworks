package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.repository.ClientRepository;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

import java.util.Optional;
@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            var clientCloned = client.clone();
            clientRepository.save(clientCloned);
            log.info("saved client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInTransaction( () -> {
            var clientOptional = clientRepository.findById(id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public Iterable<Client> findAll() {
        return transactionManager.doInTransaction( () -> {
            var clientList = clientRepository.findAll();
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
