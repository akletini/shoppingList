package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

public class UserDto implements Serializable {

    @SerializedName("id")
    private Long id;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;
    @SerializedName("staySignedIn")
    private Boolean staySignedIn;
    @SerializedName("signedIn")
    private Boolean signedIn;

    public UserDto() {
    }

    public UserDto(Long id, String username, String password, Boolean staySignedIn, Boolean signedIn, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.staySignedIn = staySignedIn;
        this.signedIn = signedIn;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getStaySignedIn() {
        return staySignedIn;
    }

    public void setStaySignedIn(Boolean staySignedIn) {
        this.staySignedIn = staySignedIn;
    }

    public Boolean getSignedIn() {
        return signedIn;
    }

    public void setSignedIn(Boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDto copy() {
        UserDto copy = new UserDto();
        copy.setId(id);
        copy.setUsername(username);
        copy.setPassword(password);
        copy.setEmail(email);
        copy.setSignedIn(signedIn);
        copy.setStaySignedIn(staySignedIn);
        return copy;
    }
}