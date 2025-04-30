package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectVersionResponseDto {

    private String versionName;

    private String dockerHubLink;

    private String githubLink;

    private String dockerCommand;

    private List<EnvParamDto> envParameters;

    private boolean isPrivate;

}
