package ru.stepagin.dockins.api.v1.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainProjectVersionServicePort;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{username}/{projectName}/versions")
@RequiredArgsConstructor
public class ProjectVersionController {

    private final ProjectDomainProjectVersionServicePort projectVersionService;

    @PostMapping
    public ResponseEntity<ProjectVersionResponseDto> createVersion(
            @PathVariable String username,
            @PathVariable String projectName,
            @RequestBody ProjectVersionCreateRequestDto requestDto) {
        ProjectVersionResponseDto responseDto = projectVersionService.createVersion(username, projectName, requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<ProjectVersionResponseDto> getDefaultVersion(
            @PathVariable String username,
            @PathVariable String projectName) {
        ProjectVersionResponseDto responseDto = projectVersionService.getDefaultProjectVersion(username, projectName);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{versionName}")
    public ResponseEntity<ProjectVersionResponseDto> getVersion(
            @PathVariable String username,
            @PathVariable String projectName,
            @PathVariable String versionName) {
        ProjectVersionResponseDto responseDto = projectVersionService.getVersion(username, projectName, versionName);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{versionName}")
    public ResponseEntity<ProjectVersionResponseDto> updateVersion(
            @PathVariable String username,
            @PathVariable String projectName,
            @PathVariable String versionName,
            @RequestBody ProjectVersionUpdateRequestDto requestDto) {
        ProjectVersionResponseDto updatedProject = projectVersionService.updateVersion(username, projectName, versionName, requestDto);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{versionName}")
    public ResponseEntity<ProjectVersionResponseDto> deleteVersion(
            @PathVariable String username,
            @PathVariable String projectName,
            @PathVariable String versionName) {
        projectVersionService.deleteVersion(username, projectName, versionName);
        return ResponseEntity.noContent().build();
    }
}
