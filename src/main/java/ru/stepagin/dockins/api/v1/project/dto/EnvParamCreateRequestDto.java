package ru.stepagin.dockins.api.v1.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EnvParamCreateRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9.-]+$")
    private String name;

    private boolean required;

    private String defaultValue;
}
