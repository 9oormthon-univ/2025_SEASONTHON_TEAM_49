package org.chanme.be.kiosk.domain.item.Itemdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.chanme.be.kiosk.domain.option.OptionValue;

@Data
@AllArgsConstructor
public class OptionValueResponseDTO {
    private Long id;
    private String label;
    private int priceDelta;


    
    public static OptionValueResponseDTO from(OptionValue optionValue) {
        return new OptionValueResponseDTO(
                optionValue.getId(),
                optionValue.getLabel(),
                optionValue.getPriceDelta()
        );
    }
}