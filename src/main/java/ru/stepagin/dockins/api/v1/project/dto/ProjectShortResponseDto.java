package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectShortResponseDto {

    private String projectName;

    private String title;

    private String authorUsername;

    private long likesCount;

    private long downloadsCount;

    private long viewsCount;

    private List<String> tags;
}