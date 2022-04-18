package com.akletini.shoppinglist.data.datastore;

import com.akletini.shoppinglist.data.model.ItemDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemDataStore implements DataStore<ItemDto> {

    private static ItemDataStore instance;
    private final List<ItemDto> items;

    private ItemDataStore() {
        items = new ArrayList<>();
    }

    public static ItemDataStore getInstance() {
        if (instance == null) {
            instance = new ItemDataStore();
        }
        return instance;
    }

    @Override
    public void addElement(ItemDto element) {
        items.add(element);
    }

    @Override
    public void modifyElement(ItemDto element) {
        for (ItemDto itemDto : items) {
            if (itemDto.getId().equals(element.getId())) {
                int index = items.indexOf(itemDto);
                items.set(index, element);
            }
        }
    }

    @Override
    public void deleteElement(ItemDto element) {
        items.remove(element);
    }

    @Override
    public List<ItemDto> getAllElements() {
        return items;
    }

    @Override
    public ItemDto getElementById(Long id) {
        return items.stream().filter(item -> item.getId().equals(id)).collect(Collectors.toList()).get(0);
    }

}
