package org.chanme.be.api.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GallaryDTO {
    private Long id;
    private String image;
    private Date date;
}
