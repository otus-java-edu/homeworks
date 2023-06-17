package ru.otus.core.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.Trade;

import java.util.List;

public interface TradeRepository extends CrudRepository<Trade, Long> {

    @Query("""
            select * from trades where user_id = :#{#userId} and symbol_id = :#{#symbolId} and time >= :#{#startTime} and id > 0 
            """)
    List<Trade> findByStartTime(@Nonnull @Param("userId")Integer userId, @Nonnull @Param("symbolId")Integer symbolId, @Nonnull @Param("startTime")Long time);
    @Modifying
    @Query("""
            insert into trades (id, user_id, symbol_id, direction, price, volume, commission, commission_asset, time, maker) 
            values (:#{#trade.pk.id}, :#{#trade.pk.userId}, :#{#trade.pk.symbolId}, :#{#trade.direction}, :#{#trade.price}, 
            :#{#trade.volume}, :#{#trade.commission}, :#{#trade.commissionAsset}, :#{#trade.time}, :#{#trade.maker})
            """)
    void saveEntry(@Param("trade") Trade trade);
}
