package com.akletini.shoppinglist.itemlist;

import com.akletini.shoppinglist.itemlist.entity.ItemList;
import org.springframework.data.repository.CrudRepository;

public interface ItemListRepository extends CrudRepository<ItemList, Long> {
}
