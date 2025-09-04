package org.chanme.be.api.dto;

import java.time.LocalDate;

public record UserView(
        Long id,
        String name,
        String memberCode,
        LocalDate birthDate
) {}
