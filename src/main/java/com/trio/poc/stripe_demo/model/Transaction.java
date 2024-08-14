package com.trio.poc.stripe_demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private User seller;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    public User getSeller() {
        return seller;
    }
    private User buyer;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    public User getBuyer() {
        return buyer;
    }

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double marketplaceFee;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction(){}

    public Transaction(User seller, User buyer, double amount, double fee){
        this.seller = seller;
        this.buyer = buyer;
        this.amount = amount;
        this.marketplaceFee = fee;
        this.timestamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getMarketplaceFee() {
        return marketplaceFee;
    }

    public void setMarketplaceFee(double marketplaceFee) {
        this.marketplaceFee = marketplaceFee;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
