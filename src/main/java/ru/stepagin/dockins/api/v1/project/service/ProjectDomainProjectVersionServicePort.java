package ru.stepagin.dockins.api.v1.project.service;

import jakarta.validation.Valid;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionUpdateRequestDto;

public interface ProjectDomainProjectVersionServicePort {

    ProjectVersionResponseDto createVersion(String username, String projectName, @Valid ProjectVersionCreateRequestDto requestDto);

    ProjectVersionResponseDto getVersion(String username, String projectName, String versionName);

    ProjectVersionResponseDto updateVersion(String username, String projectName, String versionName, ProjectVersionUpdateRequestDto requestDto);

    ProjectVersionResponseDto getDefaultProjectVersion(String username, String projectName);

    void deleteVersion(String username, String projectName, String versionName);

}
