package ru.otus.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.BNBData;

public interface BNBDataRepository  extends CrudRepository<BNBData, Long> {
}
