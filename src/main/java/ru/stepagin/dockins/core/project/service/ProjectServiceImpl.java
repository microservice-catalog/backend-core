package ru.stepagin.dockins.core.project.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.stepagin.dockins.api.v1.common.PageResponse;
import ru.stepagin.dockins.api.v1.project.dto.ProjectCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectService;
import ru.stepagin.dockins.core.auth.service.AuthService;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.entity.TagEntity;
import ru.stepagin.dockins.core.project.exception.ProjectAlreadyExistsException;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserPullRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserWatchRepository;
import ru.stepagin.dockins.core.project.service.helper.DockerCommandService;
import ru.stepagin.dockins.core.project.service.helper.MarkdownDescriptionService;
import ru.stepagin.dockins.core.project.service.helper.TagService;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.List;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectInfoRepository projectRepository;
    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final ProjectUserPullRepository projectUserPullRepository;
    private final ProjectUserWatchRepository projectUserWatchRepository;
    private final DockerCommandService dockerCommandService;
    private final MarkdownDescriptionService markdownDescriptionService;
    private final AuthService authService;
    private final ProjectSearchService projectSearchService;
    private final TagService tagService;

    @Override
    public ProjectFullResponseDto createProject(@Valid ProjectCreateRequestDto requestDto) {
        AccountEntity currentUser = authService.getCurrentUser();

        boolean exists = projectRepository.existsByAuthorAccountAndProjectName(currentUser, requestDto.getProjectName());
        if (exists) {
            throw new ProjectAlreadyExistsException("Проект с таким названием уже существует у пользователя.");
        }

        ProjectInfoEntity projectEntity = new ProjectInfoEntity();
        projectEntity.setAuthorAccount(currentUser);
        projectEntity.setProjectName(requestDto.getProjectName());
        projectEntity.setTitle(requestDto.getTitle());
        projectEntity.setDescription(markdownDescriptionService.generateDefaultDescription());
        projectEntity.setPrivate(Boolean.TRUE.equals(requestDto.getIsPrivate()));
        List<TagEntity> tags = tagService.findOrCreateTags(requestDto.getTags());
        projectEntity.setTags(tags);

        projectEntity.setCreatedOn(java.time.LocalDateTime.now());

        ProjectVersionEntity defaultVersion = new ProjectVersionEntity();
        defaultVersion.setName("default");
        defaultVersion.setProject(projectEntity);
        defaultVersion.setLinkGithub(requestDto.getGithubLink());
        defaultVersion.setLinkDockerhub(requestDto.getDockerHubLink());
        defaultVersion.setDockerCommand(dockerCommandService.generateDockerCommand(requestDto.getDockerHubLink()));

        // todo проверить, что они норм сохраняются default version
        projectRepository.save(projectEntity);

        return mapToFullDto(projectEntity);
    }

    @Override
    public PageResponse<PublicProjectShortResponseDto> getProjects(String username, PageRequest pageRequest) {
        Page<ProjectInfoEntity> page = projectRepository.findByAuthorAccountAndPrivateFalse(username, pageRequest);
        return PageResponse.of(page.map(this::mapToShortDto));
    }

    @Override
    public PageResponse<PublicProjectShortResponseDto> searchProjects(String query, List<String> tags, PageRequest pageRequest) {
        Page<ProjectInfoEntity> results = projectSearchService.searchProjects(query, tags, pageRequest);

        return PageResponse.of(results.map(this::mapToShortDto));
    }

    @Override
    public ProjectFullResponseDto getProject(String username, String projectName) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        return mapToFullDto(entity);
    }

    @Override
    @Transactional
    public ProjectFullResponseDto updateProject(
            String username,
            String projectName,
            @Valid ProjectUpdateRequestDto requestDto
    ) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        authService.belongToCurrentUserOrThrow(entity);

        if (requestDto.getTitle() != null)
            entity.setTitle(requestDto.getTitle());
        if (requestDto.getDescription() != null)
            entity.setDescription(requestDto.getDescription());
        if (requestDto.getDockerHubLink() != null)
            entity.getDefaultProjectVersion().setLinkDockerhub(requestDto.getDockerHubLink());
        if (requestDto.getGithubLink() != null)
            entity.getDefaultProjectVersion().setLinkGithub(requestDto.getGithubLink());
        if (requestDto.getIsPrivate() != null)
            entity.setPrivate(requestDto.getIsPrivate());

        entity.setUpdatedOn(java.time.LocalDateTime.now());

        projectRepository.save(entity);

        return mapToFullDto(entity);
    }

    @Override
    public void deleteProject(String username, String projectName) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        authService.belongToCurrentUserOrThrow(entity);

        entity.markAsDeleted();

        projectRepository.save(entity);
    }

    private ProjectFullResponseDto mapToFullDto(ProjectInfoEntity entity) {
        long likesCount = projectUserFavouriteRepository.countByProjectId(entity.getId());
        long downloadsCount = projectUserPullRepository.countByProjectId(entity.getId());
        long viewsCount = projectUserWatchRepository.countByProjectId(entity.getId());

        return ProjectFullResponseDto.builder()
                .projectName(entity.getProjectName())
                .title(entity.getTitle())
                .authorUsername(entity.getAuthorAccount().getUsername())
                .description(entity.getDescription())
                .tags(entity.getTagsAsString())
                .dockerHubLink(entity.getDefaultProjectVersion().getLinkDockerhub())
                .githubLink(entity.getDefaultProjectVersion().getLinkGithub())
                .dockerCommand(dockerCommandService.generateDockerCommand(entity.getDefaultProjectVersion().getLinkDockerhub()))
                .envParameters(List.of()) // todo
                .likesCount(likesCount)
                .downloadsCount(downloadsCount)
                .viewsCount(viewsCount)
                .createdOn(entity.getCreatedOn() != null ? entity.getCreatedOn().toString() : null)
                .build();
    }

    private PublicProjectShortResponseDto mapToShortDto(ProjectInfoEntity entity) {
        long likesCount = projectUserFavouriteRepository.countByProjectId(entity.getId());
        long downloadsCount = projectUserPullRepository.countByProjectId(entity.getId());
        long viewsCount = projectUserWatchRepository.countByProjectId(entity.getId());

        return PublicProjectShortResponseDto.builder()
                .projectName(entity.getProjectName())
                .title(entity.getTitle())
                .authorUsername(entity.getAuthorAccount().getUsername())
                .likesCount(likesCount)
                .downloadsCount(downloadsCount)
                .viewsCount(viewsCount)
                .tags(List.of())
                .build();
    }
}
