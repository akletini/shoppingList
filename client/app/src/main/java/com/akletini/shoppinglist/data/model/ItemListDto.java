package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemListDto {

    @SerializedName("id")
    private Long id;
    @SerializedName("itemList")
    private List<ItemDto> itemList;
    @SerializedName("market")
    private MarketDto market;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("owner")
    private UserDto owner;

    public ItemListDto(Long id, List<ItemDto> itemList, MarketDto market, String creationDate, UserDto owner) {
        this.id = id;
        this.itemList = itemList;
        this.market = market;
        this.creationDate = creationDate;
        this.owner = owner;
    }

    public ItemListDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemDto> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemDto> itemList) {
        this.itemList = itemList;
    }

    public MarketDto getMarket() {
        return market;
    }

    public void setMarket(MarketDto market) {
        this.market = market;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }
}
