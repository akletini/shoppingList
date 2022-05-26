package com.akletini.shoppinglist.itemlist;

import com.akletini.shoppinglist.itemlist.entity.ItemListEntry;
import org.springframework.data.repository.CrudRepository;

public interface ItemListEntryRepository extends CrudRepository<ItemListEntry, Long> {
}
