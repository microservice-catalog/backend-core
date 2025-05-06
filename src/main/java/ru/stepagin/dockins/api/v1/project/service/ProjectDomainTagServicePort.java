package ru.stepagin.dockins.api.v1.project.service;

import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.TagsDto;

public interface ProjectDomainTagServicePort {

    TagsDto getTags(String username, String projectName);

    @Transactional
    TagsDto updateTags(String username, String projectName, TagsDto tagsDto);

    TagsDto searchTags(String query);
}
