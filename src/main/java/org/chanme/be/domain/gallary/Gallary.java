package org.chanme.be.domain.gallary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Gallary {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String imageLink;

    @Column
    private Date date;
}
