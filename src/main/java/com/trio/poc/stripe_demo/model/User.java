package com.trio.poc.stripe_demo.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true, unique = true)
    private String stripeID;

    public User(){}

    public User(String name){
        this.name = name;
    }

    public static User createSeller(String name, String stripeID){
        var newUser = new User(name);
        newUser.setStripeID(stripeID);

        return newUser;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStripeID() {
        return stripeID;
    }

    public void setStripeID(String stripeID) {
        this.stripeID = stripeID;
    }
}


