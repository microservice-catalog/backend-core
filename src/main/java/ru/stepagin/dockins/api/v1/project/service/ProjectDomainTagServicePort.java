package ru.stepagin.dockins.api.v1.project.service;

import ru.stepagin.dockins.api.v1.project.dto.TagsDto;

public interface ProjectDomainTagServicePort {

    TagsDto getTags(String username, String projectName);

    TagsDto updateTags(String username, String projectName, TagsDto tagsDto);

}
