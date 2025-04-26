package ru.stepagin.dockins.api.v1.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmEmailDto {

    @NotBlank
    private String email;

    @NotBlank
    private String code;
}
