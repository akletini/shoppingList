package com.akletini.shoppinglist.data.datastore;

import com.akletini.shoppinglist.data.model.ItemDto;
import com.akletini.shoppinglist.data.model.MarketDto;

public class DataStoreRepository {

    public static DataStoreRepository instance;
    private final ItemDataStore itemDataStore;
    private final MarketDataStore marketDataStore;

    private DataStoreRepository() {
        this.itemDataStore = ItemDataStore.getInstance();
        this.marketDataStore = MarketDataStore.getInstance();
    }


    public static DataStoreRepository getInstance() {
        if (instance == null) {
            instance = new DataStoreRepository();
        }
        return instance;
    }

    public DataStore<?> getDataStore(Class<?> c) {
        if (c.isInstance(new ItemDto())) {
            return itemDataStore;
        } else if (c.isInstance(new MarketDto())) {
            return marketDataStore;
        }
        return null;
    }
}
