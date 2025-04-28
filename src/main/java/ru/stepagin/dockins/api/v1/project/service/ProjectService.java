package ru.stepagin.dockins.api.v1.project.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import ru.stepagin.dockins.api.v1.common.PageResponse;
import ru.stepagin.dockins.api.v1.project.dto.ProjectCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;

import java.util.List;

public interface ProjectService {

    ProjectFullResponseDto createProject(@Valid ProjectCreateRequestDto requestDto);

    PageResponse<PublicProjectShortResponseDto> getProjects(String username, PageRequest pageRequest);

    PageResponse<PublicProjectShortResponseDto> searchProjects(String query, List<String> tags, PageRequest pageRequest);

    ProjectFullResponseDto getProject(String username, String projectName);

    ProjectFullResponseDto updateProject(String username, String projectName, @Valid ProjectUpdateRequestDto requestDto);

    void deleteProject(String username, String projectName);
}
