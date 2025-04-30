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
import ru.stepagin.dockins.core.user.entity.AccountEntity;

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

    public PublicProjectShortResponseDto mapToShortDto(ProjectInfoEntity project, AccountEntity currentUser) {
        long likesCount = projectUserFavouriteRepository.countByProjectId(project.getId());
        long downloadsCount = projectUserPullRepository.countByProjectId(project.getId());
        long viewsCount = projectUserWatchRepository.countByProjectId(project.getId());
        boolean likedByMe = projectUserFavouriteRepository.existsByProjectAndUser(project, currentUser);

        return PublicProjectShortResponseDto.builder()
                .projectName(project.getProjectName())
                .title(project.getTitle())
                .authorUsername(project.getAuthorAccount().getUsername())
                .likesCount(likesCount)
                .likedByMe(likedByMe)
                .downloadsCount(downloadsCount)
                .viewsCount(viewsCount)
                .tags(project.getTagsAsString())
                .build();
    }

    public PrivateProjectShortResponseDto mapToShortDtoPrivate(ProjectInfoEntity project) {
        return PrivateProjectShortResponseDto.builder()
                .projectName(project.getProjectName())
                .title(project.getTitle())
                .authorUsername(project.getAuthorAccount().getUsername())
                .tags(project.getTagsAsString())
                .build();
    }

    public ProjectFullResponseDto mapToFullDto(ProjectInfoEntity project, AccountEntity currentUser) {
        ProjectVersionEntity defaultVersion = project.getDefaultProjectVersion();
        long likesCount = projectUserFavouriteRepository.countByProjectId(project.getId());
        long downloadsCount = projectUserPullRepository.countByProjectId(project.getId());
        long viewsCount = projectUserWatchRepository.countByProjectId(project.getId());

        List<ProjectEnvParamEntity> envParams = envParamRepository.findByProjectVersion(defaultVersion);
        boolean likedByMe = projectUserFavouriteRepository.existsByProjectAndUser(project, currentUser);

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
                .likedByMe(likedByMe)
                .downloadsCount(downloadsCount)
                .viewsCount(viewsCount)
                .createdOn(project.getCreatedOn() != null ? project.getCreatedOn().toString() : null)
                .build();
    }
}
