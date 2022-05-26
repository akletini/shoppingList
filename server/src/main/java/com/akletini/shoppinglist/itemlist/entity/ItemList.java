package com.akletini.shoppinglist.itemlist.entity;

import com.akletini.shoppinglist.auth.entity.User;
import com.akletini.shoppinglist.item.entity.Item;
import com.akletini.shoppinglist.market.entity.Market;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity(name = "itemlist")
@Table(name = "ITEMLIST")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection(targetClass = Integer.class)
    @MapKeyClass(Item.class)
    @JsonIgnore
    private Map<Item, Integer> itemAmountMap;

    @Transient
    private List<Item> itemList;

    @Transient
    private List<Integer> itemAmounts;


    @ManyToOne
    @JoinColumn(name = "marketId")
    private Market market;
    private String creationDate;
    private String name;
    @ManyToOne
    @JoinColumn(name = "OwnerId")
    private User owner;
}
