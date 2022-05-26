package com.akletini.shoppinglist.itemlist.entity;

import com.akletini.shoppinglist.auth.entity.User;
import com.akletini.shoppinglist.market.entity.Market;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany
    private List<ItemListEntry> entries;

    @ManyToOne
    @JoinColumn(name = "marketId")
    private Market market;
    private String creationDate;
    private String name;
    @ManyToOne
    @JoinColumn(name = "OwnerId")
    private User owner;
}
