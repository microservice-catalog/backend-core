package ru.stepagin.dockins.api.v1.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectVersionCreateRequestDto {

    @NotBlank
    private String versionName;

}