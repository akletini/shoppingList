package com.akletini.shoppinglist.item;

import com.akletini.shoppinglist.item.entity.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
