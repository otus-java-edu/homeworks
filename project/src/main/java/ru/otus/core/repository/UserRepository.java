package ru.otus.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
