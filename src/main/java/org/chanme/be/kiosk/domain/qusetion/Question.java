package org.chanme.be.kiosk.domain.qusetion;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.KioskType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Question {
    @Id @GeneratedValue
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private KioskType kioskType;

    @Enumerated(EnumType.STRING)
    private Packaging packaging;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private Set<QuestionItem> items = new HashSet<>();
}
