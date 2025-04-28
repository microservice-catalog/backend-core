package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EnvParamUpdateRequestDto {

    private Boolean required;

    private String defaultValue;

}
