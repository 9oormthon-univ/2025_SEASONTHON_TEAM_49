package org.chanme.be.domain.gallary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Bin {
    @Id
    @GeneratedValue
    private Long Id;

    @Column
    private String imageLink;
}
