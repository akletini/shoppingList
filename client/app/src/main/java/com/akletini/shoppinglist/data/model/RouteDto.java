package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteDto {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("itemLists")
    private List<ItemListDto> itemLists;
    @SerializedName("owner")
    private UserDto owner;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("subscribers")
    private List<UserDto> subscribers;

    public RouteDto(Long id, List<ItemListDto> itemLists, UserDto owner, String creationDate, List<UserDto> subscribers) {
        this.id = id;
        this.itemLists = itemLists;
        this.owner = owner;
        this.creationDate = creationDate;
        this.subscribers = subscribers;
    }

    public RouteDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemListDto> getItemLists() {
        return itemLists;
    }

    public void setItemLists(List<ItemListDto> itemLists) {
        this.itemLists = itemLists;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<UserDto> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<UserDto> subscribers) {
        this.subscribers = subscribers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
