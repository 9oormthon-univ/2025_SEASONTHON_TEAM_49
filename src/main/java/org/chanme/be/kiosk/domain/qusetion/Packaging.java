package org.chanme.be.kiosk.domain.qusetion;

import lombok.Getter;

@Getter
public enum Packaging {
    TAKEOUT("포장"),
    DINE_IN("매장 식사");

    private final String description;

    Packaging(String description) {
        this.description = description;
    }
}