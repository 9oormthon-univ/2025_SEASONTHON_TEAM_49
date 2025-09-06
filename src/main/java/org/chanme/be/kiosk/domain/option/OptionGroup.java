package org.chanme.be.kiosk.domain.option;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.Category;
import org.chanme.be.kiosk.domain.item.Item;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class OptionGroup {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int minSelect;
    private int maxSelect;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne @JoinColumn(name = "item_id")
    private Item item;

    @OneToMany(mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OptionValue> values = new ArrayList<>();
}

