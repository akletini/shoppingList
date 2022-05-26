package com.akletini.shoppinglist.itemlist.controller;

import com.akletini.shoppinglist.itemlist.ItemListEntryRepository;
import com.akletini.shoppinglist.itemlist.ItemListRepository;
import com.akletini.shoppinglist.itemlist.entity.ItemList;
import com.akletini.shoppinglist.itemlist.entity.ItemListEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/itemList")
public class ItemListController {

    @Autowired
    ItemListRepository itemListRepository;

    @Autowired
    ItemListEntryRepository itemListEntryRepository;

    @GetMapping(value = "/getItemList/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemList> getItemListById(@PathVariable Long id) {
        Optional<ItemList> optionalItemList = itemListRepository.findById(id);
        if (optionalItemList.isPresent()) {
            ItemList itemList = optionalItemList.get();
            return ResponseEntity.ok().body(itemList);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping(value = "/getItemLists", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<ItemList>> getItemLists() {
        List<ItemList> itemLists = (List<ItemList>) itemListRepository.findAll();
        return ResponseEntity.ok().body(itemLists);
    }

    @PostMapping(value = "/createItemList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemList> createItemList(@RequestBody ItemList itemList) {
        if (itemList == null) {
            return ResponseEntity.badRequest().body(null);
        }
        List<ItemListEntry> entries = itemList.getEntries();
        List<ItemListEntry> savedEntries = (List<ItemListEntry>) itemListEntryRepository.saveAll(entries);
        itemList.setEntries(savedEntries);
        ItemList savedItemList = itemListRepository.save(itemList);
        return ResponseEntity.ok().body(savedItemList);
    }

    @PostMapping(value = "/updateItemList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemList> updateItemList(@RequestBody ItemList itemList) {
        if (itemList == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean success = false;
        ItemList updateditemList = new ItemList();
        Optional<ItemList> saveditemListOptional = itemListRepository.findById(itemList.getId());
        if (saveditemListOptional.isPresent()) {
            List<ItemListEntry> itemListEntries = (List<ItemListEntry>) itemListEntryRepository.saveAll(itemList.getEntries());
            itemList.setEntries(itemListEntries);
            updateditemList = itemListRepository.save(itemList);
            success = true;
        }
        return success ? ResponseEntity.ok().body(updateditemList) : ResponseEntity.badRequest().body(null);
    }

    @PostMapping(value = "/deleteItemList/{id}")
    public ResponseEntity<String> deleteItemList(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean success = false;
        if (itemListRepository.existsById(id)) {
            itemListRepository.deleteById(id);
            success = true;
        }
        return success ? ResponseEntity.ok().body("Success") : ResponseEntity.badRequest().body("Deletion failed");
    }

}
