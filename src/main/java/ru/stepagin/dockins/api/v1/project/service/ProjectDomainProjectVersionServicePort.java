package ru.stepagin.dockins.api.v1.project.service;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionListResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionUpdateRequestDto;

public interface ProjectDomainProjectVersionServicePort {

    @Transactional
    ProjectVersionResponseDto createVersion(String username, String projectName, @Valid ProjectVersionCreateRequestDto requestDto);

    ProjectVersionResponseDto getVersion(String username, String projectName, String versionName);

    @Transactional
    ProjectVersionResponseDto updateVersion(String username, String projectName, String versionName, @Valid ProjectVersionUpdateRequestDto requestDto);

    ProjectVersionListResponseDto getAllProjectVersions(String username, String projectName);

    @Transactional
    void deleteVersion(String username, String projectName, String versionName);

}
