package org.chanme.be.kiosk.domain;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.option.OptionGroup;
import org.chanme.be.kiosk.domain.item.Item;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private KioskType kioskType;

    private String name;

    @OneToMany(mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OptionGroup> optionGroups = new ArrayList<>();
}

