package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemListDto {

    @SerializedName("id")
    private Long id;
    @SerializedName("itemList")
    private List<ItemDto> itemList;
    @SerializedName("itemAmounts")
    private List<Integer> itemAmounts;
    @SerializedName("name")
    private String name;
    @SerializedName("itemAmountMap")
    private Map<ItemDto, Integer> itemAmountMap;
    @SerializedName("market")
    private MarketDto market;
    @SerializedName("creationDate")
    private String creationDate;
    @SerializedName("owner")
    private UserDto owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemListDto(Long id, List<ItemDto> itemList, List<Integer> itemAmounts, Map<ItemDto, Integer> itemAmountMap, MarketDto market, String name, String creationDate, UserDto owner) {
        this.id = id;
        this.itemList = itemList;
        this.itemAmounts = itemAmounts;
        this.name = name;
        this.itemAmountMap = itemAmountMap;
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

    public Map<ItemDto, Integer> getItemAmountMap() {
        return itemAmountMap;
    }

    public void setItemAmountMap(Map<ItemDto, Integer> itemAmountMap) {
        this.itemAmountMap = itemAmountMap;
    }

    public UserDto getOwner() {
        return owner;
    }

    public List<Integer> getItemAmounts() {
        return itemAmounts;
    }

    public void setItemAmounts(List<Integer> itemAmounts) {
        this.itemAmounts = itemAmounts;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public ItemListDto copy() {
        ItemListDto copy = new ItemListDto();
        copy.setId(id);
        copy.setItemList(itemList.stream().map(ItemDto::copy).collect(Collectors.toList()));
        copy.setItemAmounts(itemAmounts);
        Map<ItemDto, Integer> copyMap = new LinkedHashMap<>();
        itemAmountMap.forEach((itemDto, integer) -> copyMap.put(itemDto.copy(), integer));
        copy.setItemAmountMap(copyMap);
        copy.setName(name);
        copy.setMarket(market.copy());
        copy.setOwner(owner.copy());
        return copy;
    }
}
