package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectVersionUpdateRequestDto {

    private String name;

    private String dockerHubLink;

    private String githubLink;

    private Boolean isPrivate;

    private Boolean makeVersionDefault;

}