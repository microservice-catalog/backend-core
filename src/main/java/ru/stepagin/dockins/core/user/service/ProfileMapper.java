package ru.stepagin.dockins.core.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
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
    private final AuthServiceImpl authServiceImpl;

    public UserPublicProfileResponseDto mapToDto(AccountEntity user, Page<ProjectInfoEntity> projects) {
        var currentUser = authServiceImpl.getCurrentUser();
        List<UUID> publicProjectsIds = projects.map(ProjectInfoEntity::getId).toList();
        long favouritesCount = projectUserFavouriteRepository.countByAccountId(user.getId());
        long viewsCount = projectUserWatchRepository.countByProjectIdList(publicProjectsIds);
        long likesCount = projectUserFavouriteRepository.countByProjectIdIn(publicProjectsIds);

        return UserPublicProfileResponseDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .publicProjects(projects.stream()
                        .map(p -> projectMapper.mapToShortDto(p, currentUser))
                        .collect(Collectors.toList()))
                .privateProjects(null)
                .favouritesCount(favouritesCount)
                .viewsCount(viewsCount)
                .likesCount(likesCount)
                .build();
    }

    public UserPublicProfileResponseDto mapToDto(
            AccountEntity user,
            Page<ProjectInfoEntity> publicProjectsPage,
            Page<ProjectInfoEntity> privateProjectsPage
    ) {
        List<UUID> publicProjectsIds = publicProjectsPage.map(ProjectInfoEntity::getId).toList();
        long favouritesCount = projectUserFavouriteRepository.countByAccountId(user.getId());
        long viewsCount = projectUserWatchRepository.countByProjectIdList(publicProjectsIds);
        long likesCount = projectUserFavouriteRepository.countByProjectIdIn(publicProjectsIds);

        return UserPublicProfileResponseDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .publicProjects(publicProjectsPage.stream()
                        .map(p -> projectMapper.mapToShortDto(p, user))
                        .collect(Collectors.toList()))
                .privateProjects(privateProjectsPage.stream()
                        .map(p -> projectMapper.mapToShortDto(p, user))
                        .collect(Collectors.toList()))
                .favouritesCount(favouritesCount)
                .viewsCount(viewsCount)
                .likesCount(likesCount)
                .build();
    }
}
