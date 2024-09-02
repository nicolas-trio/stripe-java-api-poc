package com.trio.poc.stripe_demo.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private User seller;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    public User getSeller() {
        return seller;
    }

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String stripeId;

    @Column(nullable = false)
    private String priceStripeId;

    public Product(){}

    public Product(String name, User seller, double price){
        this.name = name;
        this.seller = seller;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getPriceStripeId() {
        return priceStripeId;
    }

    public void setPriceStripeId(String priceStripeId) {
        this.priceStripeId = priceStripeId;
    }
}
