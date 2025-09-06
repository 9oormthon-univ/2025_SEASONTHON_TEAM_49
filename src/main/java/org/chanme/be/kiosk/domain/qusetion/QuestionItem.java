package org.chanme.be.kiosk.domain.qusetion;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.item.Item;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter
public class QuestionItem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private int quantity;

    @OneToMany(mappedBy = "questionItem", fetch = FetchType.LAZY)
    private Set<QuestionOption> options = new HashSet<>();
}
