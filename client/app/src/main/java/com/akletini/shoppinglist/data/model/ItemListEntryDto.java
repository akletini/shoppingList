package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

public class ItemListEntryDto {

    @SerializedName("id")
    private Long id;
    @SerializedName("item")
    private ItemDto item;
    @SerializedName("position")
    private int position;
    @SerializedName("amount")
    private int amount;

    public ItemListEntryDto() {
    }

    public ItemListEntryDto(Long id, ItemDto item, int position, int amount) {
        this.id = id;
        this.item = item;
        this.position = position;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemDto getItem() {
        return item;
    }

    public void setItem(ItemDto item) {
        this.item = item;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ItemListEntryDto copy() {
        ItemListEntryDto copy = new ItemListEntryDto();
        copy.setAmount(amount);
        copy.setItem(item.copy());
        copy.setPosition(position);
        return copy;
    }
}
