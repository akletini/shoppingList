package com.akletini.shoppinglist.data.datastore;

import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.data.model.ItemListDto;
import com.akletini.shoppinglist.data.model.MarketDto;

public class DataStoreRepository {

    public static DataStoreRepository instance;
    private final ItemDataStore itemDataStore;
    private final MarketDataStore marketDataStore;
    private final ItemListDataStore itemListDataStore;

    private DataStoreRepository() {
        this.itemDataStore = ItemDataStore.getInstance();
        this.marketDataStore = MarketDataStore.getInstance();
        this.itemListDataStore = ItemListDataStore.getInstance();
    }


    public static DataStoreRepository getInstance() {
        if (instance == null) {
            instance = new DataStoreRepository();
        }
        return instance;
    }

    public static DataStore<?> getDataStore(Class<?> c) {
        if (c.isInstance(new ItemDto())) {
            return getInstance().itemDataStore;
        } else if (c.isInstance(new MarketDto())) {
            return getInstance().marketDataStore;
        } else if (c.isInstance(new ItemListDto())) {
            return getInstance().itemListDataStore;
        }
        return getInstance().itemListDataStore; // dummy
    }
}
