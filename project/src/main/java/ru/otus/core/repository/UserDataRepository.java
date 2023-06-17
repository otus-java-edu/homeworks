package ru.otus.core.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.UserData;

import java.util.Optional;

public interface UserDataRepository extends CrudRepository<UserData, UserData.Pk> {
    @Modifying
    @Query(""" 
            update user_data set last_trade = :#{#userData.lastTrade}, last_sync_time = :#{#userData.lastSyncTime}, last_pnl_time = :#{#userData.lastPnLTime} 
            where user_id = :#{#userData.id.userId} and symbol_id = :#{#userData.id.symbolId}
            """)
    void update(@Param("userData") UserData userData);

    @Modifying
    @Query(""" 
            insert into user_data (user_id, symbol_id, last_trade, last_sync_time, last_pnl_time) 
            values (:#{#userData.id.userId}, :#{#userData.id.symbolId}, :#{#userData.lastTrade}, :#{#userData.lastSyncTime}, :#{#userData.lastPnLTime})
            """)
    void saveEntry(@Param("userData") UserData userData);

    @Query(""" 
            select * from user_data where user_id = :#{#id.userId} and symbol_id = :#{#id.symbolId}
            """)
    Optional<UserData> findById(@Nonnull @Param("id") UserData.Pk id);
}
