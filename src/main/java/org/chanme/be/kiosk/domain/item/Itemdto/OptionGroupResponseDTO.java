package org.chanme.be.kiosk.domain.item.Itemdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.chanme.be.kiosk.domain.option.OptionGroup;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OptionGroupResponseDTO {
    private Long id;
    private String name;
    private int minSelect;
    private int maxSelect;
    private List<OptionValueResponseDTO> values; // OptionValue 목록 추가

    public static OptionGroupResponseDTO from(OptionGroup optionGroup) {
        List<OptionValueResponseDTO> valueDTOs = optionGroup.getValues().stream()
                .map(OptionValueResponseDTO::from)
                .collect(Collectors.toList());

        return new OptionGroupResponseDTO(
                optionGroup.getId(),
                optionGroup.getName(),
                optionGroup.getMinSelect(),
                optionGroup.getMaxSelect(),
                valueDTOs // 변환된 DTO 목록을 포함
        );
    }
}
