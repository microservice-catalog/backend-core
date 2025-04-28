package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PrivateProjectShortResponseDto {

    private String projectName;

    private String title;

    private String authorUsername;

    private List<String> tags;
}
