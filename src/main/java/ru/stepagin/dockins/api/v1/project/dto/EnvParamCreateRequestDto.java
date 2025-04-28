package ru.stepagin.dockins.api.v1.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EnvParamCreateRequestDto {

    @NotBlank
    private String name;

    private Boolean required;

    private String defaultValue;
}
