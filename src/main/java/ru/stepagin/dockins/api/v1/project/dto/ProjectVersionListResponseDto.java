package ru.stepagin.dockins.api.v1.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProjectVersionListResponseDto {

    private List<ProjectVersionResponseDto> versions;

    private String defaultVersionName;

}
