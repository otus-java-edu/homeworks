package ru.otus.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.Symbol;

public interface SymbolRepository extends CrudRepository<Symbol, Integer> {
}
