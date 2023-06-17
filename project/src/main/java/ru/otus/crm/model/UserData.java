package ru.otus.crm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_data")
@Getter
@Setter
public class UserData implements Persistable<UserData.Pk> {
    @Id
    @Embedded.Nullable
    private final Pk id;
    @Transient
    private boolean isNew;
    private Long lastTrade;
    private Long lastSyncTime;
    @Column("last_pnl_time")
    private Long lastPnLTime;
    public UserData(Pk id, Long lastTrade, Long lastSyncTime, Long lastPnLTime){
        this.id = id;
        this.lastTrade = lastTrade;
        this.lastSyncTime = lastSyncTime;
        this.lastPnLTime = lastPnLTime;
        isNew = false;
    }
    @Override
    public boolean isNew() {
        return isNew;
    }

    public record Pk(Integer userId, Integer symbolId){}
}
