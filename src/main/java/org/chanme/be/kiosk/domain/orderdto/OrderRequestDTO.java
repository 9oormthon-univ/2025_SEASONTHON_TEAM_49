package org.chanme.be.kiosk.domain.orderdto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.qusetion.Packaging;
import org.chanme.be.kiosk.domain.qusetion.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class OrderRequestDTO {

    /** 포장 방식 (테이크아웃 vs 매장식사) */
    private Packaging packaging;

    /** 결제 방식 (현금, 카드, 모바일페이 등) */
    private PaymentMethod paymentMethod;

    /** 주문할 아이템 목록 */
    private List<OrderItemDTO> items = new ArrayList<>();

    // 기본 생성자, 전체 필드 생성자, getter/setter 생략
    public OrderRequestDTO(){

    }
}
