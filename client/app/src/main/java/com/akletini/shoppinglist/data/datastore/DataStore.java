package com.akletini.shoppinglist.data.datastore;

import java.util.List;

public interface DataStore<T> {

    void addElement(T element);

    void modifyElement(T element);

    void deleteElement(T element);

    List<T> getAllElements();

    T getElementById(Long id);
}
