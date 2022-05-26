package com.akletini.shoppinglist.data.datastore;

import com.akletini.shoppinglist.data.model.MarketDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarketDataStore implements DataStore<MarketDto> {

    private List<MarketDto> markets;

    private static MarketDataStore instance;

    private MarketDataStore() {
        markets = new ArrayList<>();
    }

    public static MarketDataStore getInstance() {
        if (instance == null) {
            instance = new MarketDataStore();
        }
        return instance;
    }

    @Override
    public void addElement(MarketDto element) {
        markets.add(element);
    }

    @Override
    public void modifyElement(MarketDto element) {
        for (MarketDto marketDto : markets) {
            if (marketDto.getId().equals(element.getId())) {
                int index = markets.indexOf(marketDto);
                markets.set(index, element);
            }
        }
    }

    @Override
    public void deleteElement(MarketDto element) {
        markets.remove(element);
    }

    @Override
    public List<MarketDto> getAllElements() {
        return markets;
    }

    @Override
    public MarketDto getElementById(Long id) {
        return markets.stream().filter(market -> market.getId().equals(id)).collect(Collectors.toList()).get(0);
    }
}
