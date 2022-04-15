package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

public class MarketDto {

    @SerializedName("id")
    private Long id;
    @SerializedName("store")
    private String store;
    @SerializedName("location")
    private String location;

    public MarketDto() {
    }

    public MarketDto(Long id, String store, String location) {
        this.id = id;
        this.store = store;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
