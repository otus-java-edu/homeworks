package ru.otus.crm.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("trades")
@Getter
@Setter
public class Trade implements Persistable<Trade.Pk> {
    @Id
    @Embedded.Nullable
    private Pk pk;
    @Transient
    private Long id;
    @SerializedName("side")
    private final Integer direction;
    private final Float price;
    @SerializedName("qty")
    private final Float volume;
    private final Float commission;
    private final String commissionAsset;
    private final Long time;
    private final Boolean maker;

    @Transient
    private boolean isNew;

    public Trade(Pk pk, Integer direction, Float price, Float volume, Float commission, String commissionAsset, Long time, Boolean maker) {
        this.pk = pk;
        this.direction = direction;
        this.price = price;
        this.volume = volume;
        this.commission = commission;
        this.commissionAsset = commissionAsset;
        this.time = time;
        this.maker = maker;
    }
    @Override
    public Pk getId(){ return pk;}

    public void setPk(Integer userId, Integer symbolId){
        this.pk = new Pk(id, userId, symbolId);
    }
    @Override
    public boolean isNew() {
        return isNew;
    }

    public record Pk(Long id, Integer userId, Integer symbolId){}
}
