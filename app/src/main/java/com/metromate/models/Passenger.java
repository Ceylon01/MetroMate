package com.metromate.models;

public class Passenger {
    private int age;
    private boolean usingCard; // 카드 사용 여부

    public Passenger(int age, boolean usingCard) {
        this.age = age;
        this.usingCard = usingCard;
    }

    public int getAge() {
        return age;
    }

    public boolean isUsingCard() {
        return usingCard;
    }

    public double getDiscountRate() {
        if (age <= 6) { // 유아 (무료)
            return 0.0;
        } else if (age >= 7 && age <= 12) { // 어린이 (50% 할인)
            return 0.5;
        } else if (age >= 13 && age <= 18) { // 청소년 (20% 할인)
            return 0.8;
        } else { // 성인
            return 1.0;
        }
    }
}
