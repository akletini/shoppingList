package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketDto marketDto = (MarketDto) o;
        return Objects.equals(id, marketDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public MarketDto copy() {
        MarketDto copy = new MarketDto();
        copy.setLocation(location);
        copy.setStore(store);
        copy.setId(id);
        return copy;
    }

    @Override
    public String toString() {
        return store + " - " + location;
    }
}
