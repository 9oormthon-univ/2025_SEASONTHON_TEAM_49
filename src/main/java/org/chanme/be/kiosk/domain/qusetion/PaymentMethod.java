package org.chanme.be.kiosk.domain.qusetion;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("현금 결제"),
    CARD("카드 결제"),
    MOBILE_PAY("모바일 페이");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}