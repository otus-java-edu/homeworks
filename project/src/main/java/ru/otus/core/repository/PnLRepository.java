package ru.otus.core.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.PnL;

import java.util.Optional;

public interface PnLRepository extends CrudRepository<PnL, PnL.Pk> {
    @Query("""
            select * from pnl where user_id = :#{#pk.userId} and symbol_id = :#{#pk.symbolId} and time = :#{#pk.time} 
            """)
    Optional<PnL> findById(@Nonnull @Param("pk") PnL.Pk pk);

    @Modifying
    @Query(""" 
            insert into pnl (user_id, symbol_id, time, profit, volume, entry_price, commissions) 
            values (:#{#pnl.pk.userId}, :#{#pnl.pk.symbolId}, :#{#pnl.pk.time}, :#{#pnl.profit}, :#{#pnl.volume}, :#{#pnl.entryPrice}, :#{#pnl.commissions})
            """)
    void saveEntry(@Param("pnl") PnL pnl);

}
