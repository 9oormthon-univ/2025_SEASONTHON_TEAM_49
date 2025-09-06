package org.chanme.be.domain.answer;

import jakarta.persistence.*;
import lombok.*;
import org.chanme.be.domain.question.Question;
import org.chanme.be.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column
    @Builder.Default
    private List<String> userAnswers = new ArrayList<>();
}
