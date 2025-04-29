package ru.stepagin.dockins.core.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.user.dto.ProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserWatchRepository;
import ru.stepagin.dockins.core.project.service.ProjectMapper;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileMapper {

    private final ProjectMapper projectMapper;
    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final ProjectUserWatchRepository projectUserWatchRepository;

    public UserPublicProfileResponseDto mapToDto(AccountEntity user, Page<ProjectInfoEntity> projects) {
        List<UUID> publicProjectsIds = projects.map(ProjectInfoEntity::getId).toList();
        long favouritesCount = projectUserFavouriteRepository.countByAccountId(user.getId());
        long viewsCount = projectUserWatchRepository.countByAccountId(user.getId());
        long likesCount = projectUserFavouriteRepository.countByProjectIdIn(publicProjectsIds);

        return UserPublicProfileResponseDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .publicProjects(projects.stream().map(projectMapper::mapToShortDto).collect(Collectors.toList()))
                .favouritesCount(favouritesCount)
                .viewsCount(viewsCount)
                .likesCount(likesCount)
                .build();
    }

    public ProfileResponseDto mapToDto(
            AccountEntity user,
            Page<ProjectInfoEntity> publicProjectsPage,
            Page<ProjectInfoEntity> privateProjectsPage
    ) {
        List<UUID> publicProjectsIds = publicProjectsPage.map(ProjectInfoEntity::getId).toList();
        long favouritesCount = projectUserFavouriteRepository.countByAccountId(user.getId());
        long viewsCount = projectUserWatchRepository.countByAccountId(user.getId());
        long likesCount = projectUserFavouriteRepository.countByProjectIdIn(publicProjectsIds);

        return ProfileResponseDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .publicProjects(publicProjectsPage.stream().map(projectMapper::mapToShortDto).collect(Collectors.toList()))
                .privateProjects(privateProjectsPage.stream().map(projectMapper::mapToShortDtoPrivate).collect(Collectors.toList()))
                .favouritesCount(favouritesCount)
                .viewsCount(viewsCount)
                .likesCount(likesCount)
                .build();
    }
}
