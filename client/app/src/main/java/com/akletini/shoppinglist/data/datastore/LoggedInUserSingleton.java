package com.akletini.shoppinglist.data.datastore;

import com.akletini.shoppinglist.data.model.UserDto;

public class LoggedInUserSingleton {

    private static LoggedInUserSingleton instance;

    private UserDto currentUser;

    private LoggedInUserSingleton() {
    }

    public static LoggedInUserSingleton getInstance() {
        if (instance == null) {
            instance = new LoggedInUserSingleton();
        }
        return instance;
    }

    public UserDto getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDto currentUser) {
        this.currentUser = currentUser;
    }
}
