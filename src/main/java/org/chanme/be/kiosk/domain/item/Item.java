package org.chanme.be.kiosk.domain.item;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.Category;
import org.chanme.be.kiosk.domain.option.OptionGroup;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int basePrice;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OptionGroup> optionGroups = new ArrayList<>();
}

