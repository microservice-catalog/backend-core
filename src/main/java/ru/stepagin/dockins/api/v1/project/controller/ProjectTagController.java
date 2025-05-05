package ru.stepagin.dockins.api.v1.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.TagsDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainTagServicePort;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{username}/{projectName}/tags")
@RequiredArgsConstructor
public class ProjectTagController {

    private final ProjectDomainTagServicePort tagService;

    @GetMapping
    public ResponseEntity<TagsDto> getTags(
            @PathVariable String username,
            @PathVariable String projectName
    ) {
        return ResponseEntity.ok(tagService.getTags(username, projectName));
    }

    @PutMapping
    public ResponseEntity<TagsDto> updateTag(
            @PathVariable String username,
            @PathVariable String projectName,
            @RequestBody TagsDto dto
    ) {
        return ResponseEntity.ok(tagService.updateTags(username, projectName, dto));
    }
}
