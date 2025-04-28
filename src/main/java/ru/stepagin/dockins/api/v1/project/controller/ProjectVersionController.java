package ru.stepagin.dockins.api.v1.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectVersionService;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{username}/{projectName}/versions")
@RequiredArgsConstructor
public class ProjectVersionController {

    private final ProjectVersionService projectVersionService;

    @PostMapping
    public ResponseEntity<ProjectVersionResponseDto> createVersion(
            @PathVariable String username,
            @PathVariable String projectName,
            @RequestBody ProjectVersionCreateRequestDto requestDto) {
        ProjectVersionResponseDto responseDto = projectVersionService.createVersion(projectName, requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    @GetMapping("/{versionName}")
    public ResponseEntity<ProjectVersionResponseDto> getVersion(
            @PathVariable String username,
            @PathVariable String projectName,
            @PathVariable String versionName) {
        ProjectVersionResponseDto responseDto = projectVersionService.getVersion(projectName, versionName);
        return ResponseEntity.ok(responseDto);
    }

//    @PatchMapping("/{versionName}") todo
//    public ResponseEntity<ProjectFullResponseDto> updateProject(
//            @PathVariable String username,
//            @PathVariable String projectName,
//            @PathVariable String versionName,
//            @RequestBody ProjectUpdateRequestDto requestDto) {
//        ProjectFullResponseDto updatedProject = projectService.updateProject(projectName, versionName, requestDto);
//        return ResponseEntity.ok(updatedProject);
//    }
}
