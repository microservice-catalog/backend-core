package ru.stepagin.dockins.api.v1.project.service;

import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;

public interface ProjectVersionService {

    ProjectVersionResponseDto createVersion(String projectName, ProjectVersionCreateRequestDto requestDto);

    ProjectVersionResponseDto getVersion(String projectName, String versionName);
}
