package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemListDto {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("market")
    private MarketDto market;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("owner")
    private UserDto owner;
    @SerializedName("entries")
    private List<ItemListEntryDto> entries;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemListDto(Long id, MarketDto market, String name, String creationDate, UserDto owner, List<ItemListEntryDto> entries) {
        this.id = id;
        this.name = name;
        this.market = market;
        this.creationDate = creationDate;
        this.owner = owner;
        this.entries = entries;
    }

    public ItemListDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<ItemListEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<ItemListEntryDto> entries) {
        this.entries = entries;
    }

    public UserDto getOwner() {
        return owner;
    }


    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public List<ItemDto> getItems() {
        List<ItemDto> resultList = new ArrayList<>();
        for (ItemListEntryDto entry : entries) {
            resultList.add(entry.getItem());
        }
        return resultList;
    }

    public void updateAmounts(List<ItemListEntryDto> entries) {
        for (ItemListEntryDto entry : entries) {
            entry.getItem().setAmount(entry.getAmount());
        }
    }

    public List<ItemListEntryDto> createEntries(List<ItemDto> items, List<Integer> amounts) {
        int count = items.size();
        List<ItemListEntryDto> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ItemListEntryDto entry = new ItemListEntryDto();
            entry.setItem(items.get(i));
            entry.setAmount(amounts.get(i));
            entry.setPosition(i);
            entries.add(entry);
        }
        return entries;
    }

    public ItemListDto copy() {
        ItemListDto copy = new ItemListDto();
        copy.setId(id);
        copy.setEntries(entries.stream().map(ItemListEntryDto::copy).collect(Collectors.toList()));
        copy.setName(name);
        copy.setMarket(market.copy());
        copy.setOwner(owner.copy());
        return copy;
    }
}
