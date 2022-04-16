package com.akletini.shoppinglist.item.controller;

import com.akletini.shoppinglist.item.ItemRepository;
import com.akletini.shoppinglist.item.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping(value = "/getItem/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item;
        Optional<Item> optionalItem = itemRepository.findById(id);
        if (optionalItem.isPresent()) {
            item = optionalItem.get();
            return ResponseEntity.ok().body(item);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping(value = "/getItems", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Item>> getItems() {
        return ResponseEntity.ok().body(itemRepository.findAll());
    }

    @PostMapping(value = "/createItem", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        if (item == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Item savedItem = itemRepository.save(item);
        return ResponseEntity.ok().body(savedItem);
    }

    @PostMapping(value = "/updateItem", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {
        if (item == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean success = false;
        Item updatedItem = new Item();
        Optional<Item> savedItemOptional = itemRepository.findById(item.getId());
        if (savedItemOptional.isPresent()) {
            updatedItem = savedItemOptional.get();
            updatedItem = itemRepository.save(updatedItem);
            success = true;
        }
        return success ? ResponseEntity.ok().body(updatedItem) : ResponseEntity.badRequest().body(null);
    }

    @PostMapping(value = "/deleteItem/{id}")
    public ResponseEntity<String> deleteItem(@RequestBody Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean success = false;
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            success = true;
        }
        return success ? ResponseEntity.ok().body("Success") : ResponseEntity.badRequest().body("Deletion failed");
    }
}
