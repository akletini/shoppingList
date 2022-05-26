package com.akletini.shoppinglist.data.datastore;

import com.akletini.shoppinglist.data.model.ItemListDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemListDataStore implements DataStore<ItemListDto> {

    private static ItemListDataStore instance;
    private final List<ItemListDto> itemLists;

    private ItemListDataStore() {
        itemLists = new ArrayList<>();
    }

    public static ItemListDataStore getInstance() {
        if (instance == null) {
            instance = new ItemListDataStore();
        }
        return instance;
    }

    @Override
    public void addElement(ItemListDto element) {
        itemLists.add(element);
    }

    @Override
    public void modifyElement(ItemListDto element) {
        for (ItemListDto itemListDto : itemLists) {
            if (itemListDto.getId().equals(element.getId())) {
                int index = itemLists.indexOf(itemListDto);
                itemLists.set(index, element);
            }
        }
    }

    @Override
    public void deleteElement(ItemListDto element) {
        itemLists.remove(element);
    }

    @Override
    public List<ItemListDto> getAllElements() {
        return itemLists;
    }

    @Override
    public ItemListDto getElementById(Long id) {
        return itemLists.stream().filter(itemList -> itemList.getId().equals(id)).collect(Collectors.toList()).get(0);
    }
}
