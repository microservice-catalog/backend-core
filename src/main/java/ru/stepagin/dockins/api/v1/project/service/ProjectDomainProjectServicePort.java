package ru.stepagin.dockins.api.v1.project.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.common.PageResponse;
import ru.stepagin.dockins.api.v1.project.dto.ProjectCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;

import java.util.List;

public interface ProjectDomainProjectServicePort {

    @Transactional
    ProjectFullResponseDto createProject(@Valid ProjectCreateRequestDto requestDto);

    PageResponse<PublicProjectShortResponseDto> getProjects(String username, PageRequest pageRequest);

    PageResponse<PublicProjectShortResponseDto> searchProjects(String query, List<String> tags, PageRequest pageRequest);

    ProjectFullResponseDto getProject(String username, String projectName);

    @Transactional
    ProjectFullResponseDto updateProject(String username, String projectName, @Valid ProjectUpdateRequestDto requestDto);

    @Transactional
    void deleteProject(String username, String projectName);

}
