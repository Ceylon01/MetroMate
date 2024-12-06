package com.metromate.fare;

import com.metromate.models.Journey;
import com.metromate.models.Passenger;

public class FareCalculatorLogic {

    public static int calculateFare(Passenger passenger, Journey journey) {
        // 기본 요금 설정
        int baseFare = passenger.isUsingCard() ? 1250 : 1350;
        int distance = journey.getDistance();

        // 거리 초과 요금 계산
        int extraFare = 0;
        if (distance > 10) {
            extraFare = ((distance - 10) / 5) * 100;
            if ((distance - 10) % 5 != 0) {
                extraFare += 100;
            }
        }

        // 환승 추가 요금 계산
        int transferFare = (journey.isTransfer() && !journey.isSameLine()) ? 1000 : 0;

        // 총 요금 계산
        int totalFare = baseFare + extraFare + transferFare;

        // 할인율 적용 (어린이, 청소년 등)
        double discountRate = passenger.getDiscountRate();
        return (int) (totalFare * discountRate);
    }
}
