package com.akletini.shoppinglist.market.controller;

import com.akletini.shoppinglist.market.MarketRepository;
import com.akletini.shoppinglist.market.entity.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/market")
public class MarketController {

    @Autowired
    MarketRepository marketRepository;

    @GetMapping(value = "/getMarket/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Market> getMarketById(@PathVariable Long id) {
        Market market;
        Optional<Market> optionalMarket = marketRepository.findById(id);
        if (optionalMarket.isPresent()) {
            market = optionalMarket.get();
            return ResponseEntity.ok().body(market);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping(value = "/getMarkets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Market>> getMarkets() {
        return ResponseEntity.ok().body(marketRepository.findAll());
    }

    @PostMapping(value = "/createMarket", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Market> createMarket(@RequestBody Market market) {
        if (market == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Market savedMarket = marketRepository.save(market);
        return ResponseEntity.ok().body(savedMarket);
    }

    @PostMapping(value = "/updateMarket", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Market> updateMarket(@RequestBody Market market) {
        if (market == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean success = false;
        Market updatedMarket = new Market();
        Optional<Market> savedMarketOptional = marketRepository.findById(market.getId());
        if (savedMarketOptional.isPresent()) {
            updatedMarket = marketRepository.save(market);
            success = true;
        }
        return success ? ResponseEntity.ok().body(updatedMarket) : ResponseEntity.badRequest().body(null);
    }

    @PostMapping(value = "/deleteMarket/{id}")
    public ResponseEntity<String> deleteMarket(@PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }
        boolean success = false;
        if (marketRepository.existsById(id)) {
            marketRepository.deleteById(id);
            success = true;
        }
        return success ? ResponseEntity.ok().body("Success") : ResponseEntity.badRequest().body("Deletion failed");
    }

}
