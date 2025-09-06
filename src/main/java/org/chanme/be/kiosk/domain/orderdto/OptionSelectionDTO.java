package org.chanme.be.kiosk.domain.orderdto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@AllArgsConstructor
@EqualsAndHashCode
public class OptionSelectionDTO {
    /**
     * OptionGroup ID (예: 토핑 추가, 사이드 변경 등)
     */
    private Long groupId;

    /**
     * 선택된 OptionValue ID 목록
     */
    private List<Long> selectedValueIds = new ArrayList<>();

    // 기본 생성자, 전체 필드 생성자, getter/setter 생략
    public OptionSelectionDTO () {

    }
}
