package org.chanme.be.kiosk.domain.option;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.chanme.be.kiosk.domain.item.Item;

@Entity
@Getter @Setter
public class OptionValue {
    @Id
    @GeneratedValue
    private Long id;

    private String label;
    private int priceDelta;
    private boolean defaultSelected;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private OptionGroup group;

    @ManyToOne @JoinColumn(name = "target_item_id")
    private Item targetItem;
}
