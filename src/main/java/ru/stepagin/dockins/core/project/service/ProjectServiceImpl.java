package ru.stepagin.dockins.core.project.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.stepagin.dockins.api.common.PageResponse;
import ru.stepagin.dockins.api.v1.project.dto.ProjectCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainProjectServicePort;
import ru.stepagin.dockins.core.auth.repository.AccountRepository;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.entity.TagEntity;
import ru.stepagin.dockins.core.project.exception.ProjectAlreadyExistsException;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.repository.*;
import ru.stepagin.dockins.core.project.service.helper.DockerCommandService;
import ru.stepagin.dockins.core.project.service.helper.MarkdownDescriptionService;
import ru.stepagin.dockins.core.project.service.helper.TagService;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.List;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectDomainProjectServicePort {

    private final ProjectInfoRepository projectRepository;
    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final ProjectUserPullRepository projectUserPullRepository;
    private final ProjectUserWatchRepository projectUserWatchRepository;
    private final DockerCommandService dockerCommandService;
    private final MarkdownDescriptionService markdownDescriptionService;
    private final AuthServiceImpl authService;
    private final ProjectSearchService projectSearchService;
    private final TagService tagService;
    private final ProjectVersionRepository projectVersionRepository;
    private final ProjectMapper projectMapper;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public ProjectFullResponseDto createProject(@Valid ProjectCreateRequestDto requestDto) {
        AccountEntity currentUser = authService.getCurrentUser();

        boolean exists = projectRepository.existsByAuthorAccountAndProjectNameToCreate(currentUser, requestDto.getProjectName());
        if (exists) {
            throw new ProjectAlreadyExistsException("Проект с таким названием уже существует у пользователя.");
        }

        ProjectInfoEntity projectEntity = new ProjectInfoEntity();
        projectEntity.setAuthorAccount(currentUser);
        projectEntity.setProjectName(requestDto.getProjectName().trim());
        projectEntity.setTitle(requestDto.getTitle().trim());
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

        projectEntity.setDefaultProjectVersion(defaultVersion);

        projectEntity.goodFieldsOrThrow();

        projectVersionRepository.save(defaultVersion);
        projectRepository.save(projectEntity);

        return projectMapper.mapToFullDto(projectEntity);
    }

    @Override
    public PageResponse<PublicProjectShortResponseDto> getProjects(String username, PageRequest pageRequest) {
        if (!accountRepository.existsByUsername(username))
            throw new UsernameNotFoundException("Не существует пользователя '" + username + "'.");
        Page<ProjectInfoEntity> page = projectRepository.findByAuthorAccountAndPrivateFalse(username, pageRequest);
        return PageResponse.of(page.map(projectMapper::mapToShortDto));
    }

    @Override
    public PageResponse<PublicProjectShortResponseDto> searchProjects(
            String query,
            List<String> tags,
            PageRequest pageRequest
    ) {
        Page<ProjectInfoEntity> results;
        if ((query == null || query.isBlank()) && (tags == null || tags.isEmpty()))
            results = projectRepository.findRecentProjects(pageRequest);
        else
            results = projectSearchService.searchProjects(query, tags, pageRequest);

        return PageResponse.of(results.map(projectMapper::mapToShortDto));
    }

    @Override
    public ProjectFullResponseDto getProject(String username, String projectName) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        if (entity.isPrivate())
            authService.belongToCurrentUserOrThrow(entity);

        return projectMapper.mapToFullDto(entity);
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

        return projectMapper.mapToFullDto(entity);
    }

    @Override
    @Transactional
    public void deleteProject(String username, String projectName) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        authService.belongToCurrentUserOrThrow(entity);

        entity.markAsDeleted();

        projectRepository.save(entity);
    }

}
