package com.trio.poc.stripe_demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private User vendor;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    public User getVendor() {
        return vendor;
    }

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = true)
    private String clientEmail;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String priceId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Long marketplaceFee;

    @Column(nullable = false)
    private String stripePaymentId;

    public Payment(){
        this.timestamp = LocalDateTime.now();
    }

    public void setVendor(User vendor) {
        this.vendor = vendor;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Long getMarketplaceFee() {
        return marketplaceFee;
    }

    public void setMarketplaceFee(Long marketplaceFee) {
        this.marketplaceFee = marketplaceFee;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public void setStripePaymentId(String stripePaymentId) {
        this.stripePaymentId = stripePaymentId;
    }
}
