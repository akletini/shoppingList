package com.akletini.shoppinglist.itemlist.entity;

import com.akletini.shoppinglist.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "itemListEntry")
@Table(name = "ITEMLISTENTRY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemListEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    private int position;

    private int amount;
}
