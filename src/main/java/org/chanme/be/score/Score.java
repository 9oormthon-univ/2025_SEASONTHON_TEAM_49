package org.chanme.be.score;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.domain.user.User; // 제공해주신 User 엔티티로 변경
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 점수를 획득한 사용자 (User 엔티티와 N:1 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // DB에는 user_id 외래 키 컬럼이 생성됩니다.
    private User user;

    /**
     * 문제의 종류 (키오스크, 폰 기능 등)
     */
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    /**
     * 어떤 문제에 대한 점수인지 식별하기 위한 ID
     */
    private Long questionId;

    private int value; // 계산된 점수

    private long elapsedTimeInSeconds; // 걸린 시간(초)

    private LocalDateTime createdAt; // 점수 기록 시간

    /* 객체가 데이터베이스에 새로 저장되기 직전에, 이 메서드를 딱 한 번만 자동으로 실행 */
    @PrePersist
    public void createdAt() {
        this.createdAt = LocalDateTime.now();
    }
}
