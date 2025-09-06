package org.chanme.be.domain.message;

import jakarta.persistence.*;
import lombok.*;
import org.chanme.be.domain.contact.Contact;
import org.chanme.be.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Message {
    @Id
    @GeneratedValue
    private Long id;

    // 연락처에 있는 사람
    @ManyToOne
    @JoinColumn(name="user_id")
    private User sender;

    @OneToMany
    @JoinColumn(name="contact_id")
    @Builder.Default
    private List<Contact> recipient  = new ArrayList<Contact>();

    @Column
    private String content;
}
