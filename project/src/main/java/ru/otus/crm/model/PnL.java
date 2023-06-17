package ru.otus.crm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("pnl")
@Getter
@Setter
public class PnL  implements Persistable<PnL.Pk> {
    @Id
    @Embedded.Nullable
    private PnL.Pk pk;
    private final Float profit;
    private final Float volume;
    private final Float entryPrice;
    private final Float commissions;

    public PnL(Pk pk, Float profit, Float volume, Float entryPrice, Float commissions) {
        this.pk = pk;
        this.profit = profit;
        this.volume = volume;
        this.entryPrice = entryPrice;
        this.commissions = commissions;
    }


    @Override
    public Pk getId() {
        return null;
    }
    @Transient
    private boolean isNew;
    @Override
    public boolean isNew() {
        return isNew;
    }

    public record Pk(Integer userId, Integer symbolId, Long time){}
}
