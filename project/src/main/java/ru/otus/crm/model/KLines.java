package ru.otus.crm.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KLines {
    @SerializedName("side")
    private final Integer direction;
    private final Float price;
}
