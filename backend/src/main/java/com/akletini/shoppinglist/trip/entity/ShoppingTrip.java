package com.akletini.shoppinglist.trip.entity;

import com.akletini.shoppinglist.auth.entity.User;
import com.akletini.shoppinglist.itemlist.entity.ItemList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "shoppingtrip")
@Table(name = "SHOPPINGTRIP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    private List<ItemList> itemLists;
    private User owner;
    private Date creationDate;
    @ElementCollection
    private List<User> subscribers;
}
