package ru.stepagin.dockins.api.v1.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainProjectEnvParamServicePort;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{username}/{projectName}/versions/{versionName}/env")
@RequiredArgsConstructor
public class ProjectEnvParamController {

    private final ProjectDomainProjectEnvParamServicePort projectEnvParamService;

    @PostMapping
    public ResponseEntity<EnvParamDto> addEnvParam(
            @PathVariable String username,
            @PathVariable String projectName,
            @PathVariable String versionName,
            @RequestBody EnvParamCreateRequestDto requestDto) {
        EnvParamDto responseDto = projectEnvParamService.addEnvParam(username, projectName, versionName, requestDto);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PatchMapping("/{paramName}")
    public ResponseEntity<EnvParamDto> updateEnvParam(
            @PathVariable String username,
            @PathVariable String projectName,
            @PathVariable String versionName,
            @PathVariable String paramName,
            @RequestBody EnvParamUpdateRequestDto requestDto) {
        EnvParamDto responseDto = projectEnvParamService.updateEnvParam(username, projectName, versionName, paramName, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{paramName}")
    public ResponseEntity<Void> deleteEnvParam(
            @PathVariable String username,
            @PathVariable String projectName,
            @PathVariable String versionName,
            @PathVariable String paramName) {
        projectEnvParamService.deleteEnvParam(username, projectName, versionName, paramName);
        return ResponseEntity.noContent().build();
    }
}
