package org.chanme.be.kiosk.domain.orderdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCheckResultDTO {
    private boolean correct;
    private List<MismatchDTO> mismatches = new ArrayList<>();

    @Getter
    @Builder // 빌더 패턴으로 생성자보다 유연하게 객체 생성
    public static class MismatchDTO {
        private MismatchType type;      // [필수] 어떤 종류의 오류인지
        private Long itemId;            // [선택] 오류가 발생한 아이템 ID
        private String itemName;        // [선택] 프론트에서 보여주기 편하도록 아이템 이름 추가
        private Object expected;        // [선택] 정답 값
        private Object actual;          // [선택] 사용자의 선택 값
        private String message;         // [필수] 기본적으로 보여줄 친절한 메시지
    }
}