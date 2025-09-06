package org.chanme.be.kiosk.domain.orderdto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@AllArgsConstructor
@EqualsAndHashCode(exclude = "quantity")  //quantity를 제외하고 나머지 필드 비교
public class OrderItemDTO {
    /** 선택한 메뉴 아이템 ID */
    private Long itemId;

    /** 수량 */
    private int quantity;

    /** 그룹별 옵션 선택 정보 */
    private List<OptionSelectionDTO> selections = new ArrayList<>();

    // 기본 생성자, 전체 필드 생성자, getter/setter 생략
    public OrderItemDTO(){

    }
}
