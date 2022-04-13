package com.akletini.shoppinglist.trip;

import com.akletini.shoppinglist.trip.entity.ShoppingTrip;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingTripRepository extends CrudRepository<ShoppingTrip, Long> {
}
