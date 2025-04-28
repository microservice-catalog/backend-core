package ru.stepagin.dockins.api.v1.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileUpdateRequestDto {

    @Size(max = 100)
    private String fullName;

    @Size(max = 500)
    private String description;
}