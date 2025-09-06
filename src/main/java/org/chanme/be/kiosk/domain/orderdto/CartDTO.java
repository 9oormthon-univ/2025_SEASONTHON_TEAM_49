package org.chanme.be.kiosk.domain.orderdto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartDTO {

    // 사용자가 선택한 포장, 결제 방식 등도 필요 시 추가 가능
    // private Packaging packaging;
    // private PaymentMethod paymentMethod;

    private List<OrderItemDTO> items = new ArrayList<>();

    public void addItem(OrderItemDTO newItem) {
        // 옵션까지 완전히 동일한 아이템이 이미 있는지 확인
        // 있다면 수량만 증가, 없다면 새로 추가하는 로직을 넣으면 더 좋습니다.
        // 여기서는 간단하게 그냥 추가하겠습니다.
        this.items.add(newItem);
    }
}
