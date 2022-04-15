package com.akletini.shoppinglist.route.entity;

import com.akletini.shoppinglist.auth.entity.User;
import com.akletini.shoppinglist.itemlist.entity.ItemList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "route")
@Table(name = "ROUTE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ElementCollection
    private List<ItemList> itemLists;
    private User owner;
    private String creationDate;
    @ElementCollection
    private List<User> subscribers;
}
