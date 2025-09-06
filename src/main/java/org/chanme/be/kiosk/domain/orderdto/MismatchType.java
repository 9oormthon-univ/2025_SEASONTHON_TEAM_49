package org.chanme.be.kiosk.domain.orderdto;

public enum MismatchType {
    // 전체 주문 관련 오류
    PACKAGING,          // 포장 방식 오류
    PAYMENT_METHOD,     // 결제 방식 오류

    // 아이템 관련 오류
    ITEM_MISSING,       // 정답 아이템 누락
    ITEM_UNNECESSARY,   // 불필요한 아이템 추가
    ITEM_QUANTITY,      // 아이템 수량 오류

    // 옵션 관련 오류
    OPTION_SELECTION,   // 옵션 선택 오류
    OPTION_GROUP_MISSING// 옵션 그룹 누락
}