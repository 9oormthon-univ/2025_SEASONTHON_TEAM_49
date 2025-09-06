package org.chanme.be.kiosk.domain.qusetion;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.option.OptionGroup;
import org.chanme.be.kiosk.domain.option.OptionValue;

@Entity
@Getter @Setter
public class QuestionOption {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_item_id", nullable = false)
    private QuestionItem questionItem;

    @ManyToOne @JoinColumn(name = "group_id", nullable = false)
    private OptionGroup group;

    @ManyToOne @JoinColumn(name = "value_id", nullable = false)
    private OptionValue value;
}
