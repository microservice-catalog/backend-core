package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectFullResponseDto {

    private String projectName;

    private String title;

    private String authorUsername;

    private String description;

    private List<String> tags;

    private String dockerHubLink;

    private String githubLink;

    private String dockerCommand;

    private List<EnvParamDto> envParameters;

    private long likesCount;

    private long downloadsCount;

    private long viewsCount;

    private String createdOn;
}
