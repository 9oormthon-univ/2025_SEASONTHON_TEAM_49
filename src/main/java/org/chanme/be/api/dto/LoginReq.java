package org.chanme.be.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReq(@NotBlank String memberCode) {}
