package org.chanme.be.domain.message;

import jakarta.persistence.*;
import lombok.*;
import org.chanme.be.domain.contact.Contact;
import org.chanme.be.domain.question.Question;
import org.chanme.be.domain.user.User;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InitialMessage {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name="question_id")
    private Question question;

    // 연락처에 있는 사람
    @ManyToOne
    @JoinColumn(name="contact_id")
    private Contact sender;

    @OneToOne
    @JoinColumn(name="user_id")
    private User recipient;

    @Column
    private String content;
}
