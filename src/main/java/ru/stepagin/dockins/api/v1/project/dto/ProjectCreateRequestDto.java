package ru.stepagin.dockins.api.v1.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectCreateRequestDto {

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    @Size(min = 5, max = 50)
    private String projectName;

    private List<String> tags;

    private String dockerHubLink;

    private String githubLink;

    private Boolean isPrivate;
}