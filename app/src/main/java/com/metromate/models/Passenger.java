package com.metromate.models;

public class Passenger {
    private double discountRate;
    private boolean isUsingCard;

    public Passenger(double discountRate, boolean isUsingCard) {
        this.discountRate = discountRate;
        this.isUsingCard = isUsingCard;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public boolean isUsingCard() {
        return isUsingCard;
    }
}