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

    @Column(nullable = false)
    private String email;

    @Column(nullable = true, unique = true)
    private String stripeID;

    @Column(nullable = true)
    private Boolean completedOnboarding;

    public User(){}

    public User(String name, String email){
        this.name = name; this.email= email;
    }

    public static User createSeller(String name, String stripeID, String email){
        var newUser = new User(name, email);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStripeID() {
        return stripeID;
    }

    public void setStripeID(String stripeID) {
        this.stripeID = stripeID;
    }

    public Boolean getCompletedOnboarding() {
        return completedOnboarding;
    }

    public void setCompletedOnboarding(Boolean completedOnboarding) {
        this.completedOnboarding = completedOnboarding;
    }
}


