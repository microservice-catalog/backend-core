package ru.stepagin.dockins.core.project.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.TagsDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainTagServicePort;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.TagEntity;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.service.helper.TagService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectDomainTagServiceImpl implements ProjectDomainTagServicePort {

    private final ProjectInfoRepository projectRepository;
    private final AuthServiceImpl authService;
    private final TagService tagService;

    @Override
    public TagsDto getTags(String username, String projectName) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);
        return new TagsDto(entity.getTagsAsString());
    }

    @Override
    @Transactional
    public TagsDto updateTags(String username, String projectName, @Valid TagsDto tagsDto) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);
        authService.belongToCurrentUserOrThrow(entity);
        entity.setTags(tagService.findOrCreateTags(tagsDto.getTags()));
        projectRepository.save(entity);
        return new TagsDto(entity.getTagsAsString());
    }

    @Override
    public TagsDto searchTags(String query) {
        Pageable pageRequest = PageRequest.of(0, 20);

        var tags = tagService.searchTags(query, pageRequest).stream()
                .map(TagEntity::getName)
                .toList();

        return new TagsDto(tags);
    }

}
