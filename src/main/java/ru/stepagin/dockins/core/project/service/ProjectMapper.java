package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.PrivateProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.core.project.entity.ProjectEnvParamEntity;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.repository.ProjectEnvParamRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserPullRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserWatchRepository;
import ru.stepagin.dockins.core.project.service.helper.DockerCommandService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMapper {

    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final ProjectUserPullRepository projectUserPullRepository;
    private final ProjectUserWatchRepository projectUserWatchRepository;
    private final DockerCommandService dockerCommandService;
    private final ProjectEnvParamRepository envParamRepository;
    private final EnvMapper envMapper;

    public PublicProjectShortResponseDto mapToShortDto(ProjectInfoEntity entity) {
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
                .tags(entity.getTagsAsString())
                .build();
    }

    public PrivateProjectShortResponseDto mapToShortDtoPrivate(ProjectInfoEntity entity) {
        return PrivateProjectShortResponseDto.builder()
                .projectName(entity.getProjectName())
                .title(entity.getTitle())
                .authorUsername(entity.getAuthorAccount().getUsername())
                .tags(entity.getTagsAsString())
                .build();
    }

    public ProjectFullResponseDto mapToFullDto(ProjectInfoEntity project) {
        ProjectVersionEntity defaultVersion = project.getDefaultProjectVersion();
        long likesCount = projectUserFavouriteRepository.countByProjectId(project.getId());
        long downloadsCount = projectUserPullRepository.countByProjectId(project.getId());
        long viewsCount = projectUserWatchRepository.countByProjectId(project.getId());

        List<ProjectEnvParamEntity> envParams = envParamRepository.findByProjectVersion(defaultVersion);

        return ProjectFullResponseDto.builder()
                .projectName(project.getProjectName())
                .title(project.getTitle())
                .authorUsername(project.getAuthorAccount().getUsername())
                .description(project.getDescription())
                .tags(project.getTagsAsString())
                .dockerHubLink(defaultVersion.getLinkDockerhub())
                .githubLink(defaultVersion.getLinkGithub())
                .dockerCommand(dockerCommandService
                        .generateDockerCommand(defaultVersion.getLinkDockerhub())
                )
                .envParameters(envParams.stream()
                        .map(envMapper::convertToDto)
                        .collect(Collectors.toList())
                )
                .likesCount(likesCount)
                .downloadsCount(downloadsCount)
                .viewsCount(viewsCount)
                .createdOn(project.getCreatedOn() != null ? project.getCreatedOn().toString() : null)
                .build();
    }
}
