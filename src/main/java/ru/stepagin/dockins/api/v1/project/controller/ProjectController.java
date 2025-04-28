package ru.stepagin.dockins.api.v1.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.common.PageResponse;
import ru.stepagin.dockins.api.v1.project.dto.ProjectCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectFullResponseDto> createProject(@RequestBody ProjectCreateRequestDto requestDto) {
        ProjectFullResponseDto responseDto = projectService.createProject(requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<PublicProjectShortResponseDto>> searchProjects(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "t", required = false) List<String> tags,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit
    ) {
        PageResponse<PublicProjectShortResponseDto> projects = projectService.searchProjects(query, tags, PageRequest.of(page, limit));
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{username}")
    public ResponseEntity<PageResponse<PublicProjectShortResponseDto>> getUserProjects(
            @PathVariable String username,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit) {
        PageResponse<PublicProjectShortResponseDto> projects = projectService.getProjects(username, PageRequest.of(page, limit));
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{username}/{projectName}")
    public ResponseEntity<ProjectFullResponseDto> getProject(
            @PathVariable String username,
            @PathVariable String projectName) {
        ProjectFullResponseDto project = projectService.getProject(username, projectName);
        return ResponseEntity.ok(project);
    }

    @PatchMapping("/{username}/{projectName}")
    public ResponseEntity<ProjectFullResponseDto> updateProject(
            @PathVariable String username,
            @PathVariable String projectName,
            @RequestBody ProjectUpdateRequestDto requestDto) {
        ProjectFullResponseDto updatedProject = projectService.updateProject(username, projectName, requestDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{username}/{projectName}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable String username,
            @PathVariable String projectName) {
        projectService.deleteProject(username, projectName);
        return ResponseEntity.noContent().build();
    }
}
