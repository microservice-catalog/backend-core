package ru.stepagin.dockins.api.v1.project.service;

import org.springframework.data.domain.PageRequest;
import ru.stepagin.dockins.api.v1.project.dto.ProjectCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectUpdateRequestDto;

import java.util.List;

public interface ProjectService {

    ProjectFullResponseDto createProject(ProjectCreateRequestDto requestDto);

    List<ProjectShortResponseDto> searchProjects(String query, List<String> tags, PageRequest pageRequest);

    ProjectFullResponseDto getProject(String username, String projectName);

    ProjectFullResponseDto updateProject(String projectName, ProjectUpdateRequestDto requestDto);

    void deleteProject(String projectName);
}
