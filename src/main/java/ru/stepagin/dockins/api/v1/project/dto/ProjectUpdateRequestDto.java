package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectUpdateRequestDto {

    private String title;

    private String description;

    private List<String> tags;

    private String dockerHubLink;

    private String githubLink;

    private Boolean isPrivate;
}