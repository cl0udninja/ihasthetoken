package com.jelohazi.ihasthetoken.domain;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private static final long serialVersionUID = 5012615976624331341L;

    private UUID id;

    private String name;

    private String email;

    public User() {
        this.id = UUID.randomUUID();
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.id = UUID.randomUUID();
        ;
    }

    public User(UUID id, String name, String email) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public User(String id, String name, String email) {
        this.name = name;
        this.email = email;
        this.id = UUID.fromString(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
    }

}
