package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.ProjectCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectService;
import ru.stepagin.dockins.core.auth.service.AuthService;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.exception.ProjectAlreadyExistsException;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserPullRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserWatchRepository;
import ru.stepagin.dockins.core.project.service.helper.DockerCommandService;
import ru.stepagin.dockins.core.project.service.helper.MarkdownDescriptionService;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
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

    @Override
    public ProjectFullResponseDto createProject(ProjectCreateRequestDto requestDto) {
        AccountEntity currentUser = authService.getCurrentUser();

        boolean exists = projectRepository.existsByAuthorAccountAndProjectName(currentUser, requestDto.getProjectName());
        if (exists) {
            throw new ProjectAlreadyExistsException("Проект с таким названием уже существует у пользователя.");
        }

        ProjectInfoEntity entity = new ProjectInfoEntity();
        entity.setProjectName(requestDto.getProjectName());
        entity.setTitle(requestDto.getTitle());
        entity.setDescription(requestDto.getDescription() != null ? requestDto.getDescription() : markdownDescriptionService.generateDefaultDescription());
        entity.setAuthorAccount(currentUser);
        entity.setIsPrivate(Boolean.TRUE.equals(requestDto.getIsPrivate()));

        entity.setCreatedOn(java.time.LocalDateTime.now());

        projectRepository.save(entity);

        // Привязка тегов и генерация docker команды будет потом

        return mapToFullDto(entity);
    }

    @Override
    public List<ProjectShortResponseDto> searchProjects(String query, List<String> tags, PageRequest pageRequest) {
        List<ProjectInfoEntity> results = projectSearchService.searchProjects(query, tags, pageRequest);

        return results.stream().map(this::mapToShortDto).collect(Collectors.toList());
    }

    @Override
    public ProjectFullResponseDto getProject(String username, String projectName) {
        ProjectInfoEntity entity = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден."));

        return mapToFullDto(entity);
    }

    @Override
    public ProjectFullResponseDto updateProject(String projectName, ProjectUpdateRequestDto requestDto) {
        AccountEntity currentUser = authService.getCurrentUser();

        ProjectInfoEntity entity = projectRepository.findByAuthorAccountAndProjectName(currentUser.getUsername(), projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден."));

        if (requestDto.getTitle() != null) entity.setTitle(requestDto.getTitle());
        if (requestDto.getDescription() != null) entity.setDescription(requestDto.getDescription());
        if (requestDto.getDockerHubLink() != null)
            entity.setLinkDockerhub(requestDto.getDockerHubLink()); // todo: ссылки у версий
        if (requestDto.getGithubLink() != null) entity.setLinkGithub(requestDto.getGithubLink());
        if (requestDto.getIsPrivate() != null) entity.setIsPrivate(requestDto.getIsPrivate());

        entity.setUpdatedOn(java.time.LocalDateTime.now());

        projectRepository.save(entity);

        return mapToFullDto(entity);
    }

    @Override
    public void deleteProject(String projectName) {
        AccountEntity currentUser = authService.getCurrentUser();

        ProjectInfoEntity entity = projectRepository.findByAuthorAccountAndProjectName(currentUser.getUsername(), projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден."));

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
                .tags(entity.getTagsAsString()) // позже будет загрузка реальных тегов
                .dockerHubLink(entity.getLinkDockerhub()) // todo
                .githubLink(entity.getLinkGithub())
                .dockerCommand(dockerCommandService.generateDockerCommand(entity.getLinkDockerhub()))
                .envParameters(List.of()) // todo
                .likesCount(likesCount)
                .downloadsCount(downloadsCount)
                .viewsCount(viewsCount)
                .createdOn(entity.getCreatedOn() != null ? entity.getCreatedOn().toString() : null)
                .build();
    }

    private ProjectShortResponseDto mapToShortDto(ProjectInfoEntity entity) {
        long likesCount = projectUserFavouriteRepository.countByProjectId(entity.getId());
        long downloadsCount = projectUserPullRepository.countByProjectId(entity.getId());
        long viewsCount = projectUserWatchRepository.countByProjectId(entity.getId());

        return ProjectShortResponseDto.builder()
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
