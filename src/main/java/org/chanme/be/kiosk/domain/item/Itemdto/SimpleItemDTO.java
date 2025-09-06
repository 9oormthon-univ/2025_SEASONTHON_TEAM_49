package org.chanme.be.kiosk.domain.item.Itemdto;

import lombok.Data;
import org.chanme.be.kiosk.domain.item.Item;

@Data
public class SimpleItemDTO {
    private Long id;
    private String name;
    private int basePrice;

    public static SimpleItemDTO from(Item item) {
        SimpleItemDTO dto = new SimpleItemDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setBasePrice(item.getBasePrice());
        return dto;

    }
}
