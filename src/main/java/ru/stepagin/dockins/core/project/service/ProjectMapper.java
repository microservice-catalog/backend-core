package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.PrivateProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectFullResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserPullRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserWatchRepository;
import ru.stepagin.dockins.core.project.service.helper.DockerCommandService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectMapper {

    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final ProjectUserPullRepository projectUserPullRepository;
    private final ProjectUserWatchRepository projectUserWatchRepository;
    private final DockerCommandService dockerCommandService;

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

    public ProjectFullResponseDto mapToFullDto(ProjectInfoEntity entity) {
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
                .dockerCommand(dockerCommandService
                        .generateDockerCommand(entity.getDefaultProjectVersion().getLinkDockerhub())
                )
                .envParameters(List.of()) // TODO
                .likesCount(likesCount)
                .downloadsCount(downloadsCount)
                .viewsCount(viewsCount)
                .createdOn(entity.getCreatedOn() != null ? entity.getCreatedOn().toString() : null)
                .build();
    }
}
