package com.akletini.shoppinglist.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @SerializedName("staySignedIn")
    private Boolean staySignedIn;
    @SerializedName("signedIn")
    private Boolean signedIn;

    public UserDto() {}

    public UserDto(Long id, String username, String password, Boolean staySignedIn, Boolean signedIn) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.staySignedIn = staySignedIn;
        this.signedIn = signedIn;
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
}