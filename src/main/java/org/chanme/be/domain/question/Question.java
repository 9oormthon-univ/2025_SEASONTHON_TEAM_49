package org.chanme.be.domain.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Integer kindId;

    @Column
    private String question;

    @Column
    @Builder.Default
    private List<HashMap<String, String>> answers = new ArrayList<>();
}
