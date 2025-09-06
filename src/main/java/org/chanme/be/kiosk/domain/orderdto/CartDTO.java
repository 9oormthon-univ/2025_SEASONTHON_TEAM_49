package org.chanme.be.kiosk.domain.orderdto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class CartDTO {

    // 사용자가 선택한 포장, 결제 방식 등도 필요 시 추가 가능
    // private Packaging packaging;
    // private PaymentMethod paymentMethod;

    private List<OrderItemDTO> items = new ArrayList<>();

    public void addItem(OrderItemDTO newItem) {
        // newItem과 itemId 및 selections가 모두 동일한 기존 아이템이 있는지 찾습니다.
        Optional<OrderItemDTO> existingItemOpt = this.items.stream()
                .filter(existingItem -> existingItem.equals(newItem))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // 동일한 아이템이 있다면, 기존 아이템의 수량을 증가시킵니다.
            OrderItemDTO existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
        } else {
            // 동일한 아이템이 없다면, 새로운 아이템을 리스트에 추가합니다.
            this.items.add(newItem);
        }
    }
}
