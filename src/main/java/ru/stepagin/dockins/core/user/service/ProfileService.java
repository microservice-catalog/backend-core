package ru.stepagin.dockins.core.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.ProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;
import ru.stepagin.dockins.core.auth.service.AuthService;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserWatchRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final AuthService authService;
    private final ProjectInfoRepository projectInfoRepository;
    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final ProjectUserWatchRepository projectUserWatchRepository;

    public ProfileResponseDto getCurrentProfile() {
        AccountEntity currentUser = authService.getCurrentUser();
        List<ProjectInfoEntity> projects = projectInfoRepository.findByAuthorAccountAndDeletedFalse(currentUser);
        long favouritesCount = projectUserFavouriteRepository.countByAccountId(currentUser.getId());
        long viewsCount = projectUserWatchRepository.countByAccountId(currentUser.getId());

        List<UUID> publicProjects = projects.stream().map(ProjectInfoEntity::getId).toList();
        long likesCount = projectUserFavouriteRepository.countByProjectIdIn(publicProjects);
        return ProfileResponseDto.builder()
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .description(currentUser.getDescription())
                .avatarUrl(currentUser.getAvatarUrl())
                .publicProjects(projects.stream().map(this::mapToShortDto).collect(Collectors.toList()))
                .favouritesCount(favouritesCount)
                .viewsCount(viewsCount)
                .likesCount(likesCount)
                .build();
    }

    public ProfileResponseDto updateCurrentProfile(ProfileUpdateRequestDto dto) {
        AccountEntity currentUser = authService.getCurrentUser();

        if (dto.getFullName() != null)
            currentUser.setFullName(dto.getFullName());
        if (dto.getDescription() != null)
            currentUser.setDescription(dto.getDescription());

        authService.updateCurrentUserData(currentUser);

        return getCurrentProfile();
    }

    private ProjectShortResponseDto mapToShortDto(ProjectInfoEntity entity) { // TODO ОБЪЕДИНИТЬ МАППЕРЫ
        return ProjectShortResponseDto.builder()
                .projectName(entity.getProjectName())
                .title(entity.getTitle())
                .authorUsername(entity.getAuthorAccount().getUsername())
                .likesCount(0)  // todo
                .downloadsCount(0)  // todo
                .viewsCount(0)  // todo
                .tags(List.of())
                .build();
    }
}