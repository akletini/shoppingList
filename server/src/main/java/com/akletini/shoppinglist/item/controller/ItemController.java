package com.akletini.shoppinglist.item.controller;

import com.akletini.shoppinglist.item.ItemRepository;
import com.akletini.shoppinglist.item.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping(value = "/getItem/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> getItemById(Long id) {
        Item item;
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
            return ResponseEntity.ok().body(item);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
