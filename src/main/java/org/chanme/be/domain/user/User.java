package org.chanme.be.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 이름 (필수) */
    @NotBlank
    private String name;

    /** 전화번호: 숫자/하이픈 허용 (중복 금지) */
    @NotBlank
    @Pattern(regexp = "^[0-9\\-]{9,15}$")
    @Column(unique = true, nullable = false)
    private String phone;

    /** 생년월일(선택) */
    private LocalDate birthDate;

    /** 회원번호: 이름+동물+YY/MM/DD 충돌 단계 (중복 금지) */
    @Column(unique = true, nullable = false, length = 64)
    private String memberCode;

    @Column(name= "hap_score")
    private Integer hapScore;

    /** 접근성 설정 (예: 글자 배율) */
    private Integer fontScale;
}
