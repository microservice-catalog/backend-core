package ru.stepagin.dockins.core.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.PrivateProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;
import ru.stepagin.dockins.api.v1.user.service.UserDomainProfileServicePort;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
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
public class ProfileServiceImpl implements UserDomainProfileServicePort {

    private final AuthServiceImpl authService;
    private final ProjectInfoRepository projectInfoRepository;
    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final ProjectUserWatchRepository projectUserWatchRepository;

    @Override
    public ProfileResponseDto getCurrentProfile() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        AccountEntity currentUser = authService.getCurrentUser();
        Page<ProjectInfoEntity> publicProjectsPage = projectInfoRepository.findByAuthorAccountAndPrivateFalse(currentUser, pageRequest);
        Page<ProjectInfoEntity> privateProjectsPage = projectInfoRepository.findByAuthorAccountAndPrivateTrue(currentUser, pageRequest);
        long favouritesCount = projectUserFavouriteRepository.countByAccountId(currentUser.getId());
        long viewsCount = projectUserWatchRepository.countByAccountId(currentUser.getId());

        List<UUID> publicProjectsIds = publicProjectsPage.map(ProjectInfoEntity::getId).toList();
        long likesCount = projectUserFavouriteRepository.countByProjectIdIn(publicProjectsIds);
        return ProfileResponseDto.builder()
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .description(currentUser.getDescription())
                .avatarUrl(currentUser.getAvatarUrl())
                .publicProjects(publicProjectsPage.stream().map(this::mapToShortDto).collect(Collectors.toList()))
                .privateProjects(privateProjectsPage.stream().map(this::mapToShortDtoPrivate).collect(Collectors.toList()))
                .favouritesCount(favouritesCount)
                .viewsCount(viewsCount)
                .likesCount(likesCount)
                .build();
    }

    @Transactional
    @Override
    public void updateCurrentProfile(@Valid ProfileUpdateRequestDto dto) {
        AccountEntity currentUser = authService.getCurrentUser();

        if (dto.getFullName() != null)
            currentUser.setFullName(dto.getFullName().trim());
        if (dto.getDescription() != null)
            currentUser.setDescription(dto.getDescription().trim());

        authService.updateCurrentUserData(currentUser);
    }

    private PublicProjectShortResponseDto mapToShortDto(ProjectInfoEntity entity) { // TODO ОБЪЕДИНИТЬ МАППЕРЫ
        return PublicProjectShortResponseDto.builder()
                .projectName(entity.getProjectName())
                .title(entity.getTitle())
                .authorUsername(entity.getAuthorAccount().getUsername())
                .likesCount(0)  // todo
                .downloadsCount(0)  // todo
                .viewsCount(0)  // todo
                .tags(entity.getTagsAsString())
                .build();
    }

    private PrivateProjectShortResponseDto mapToShortDtoPrivate(ProjectInfoEntity entity) { // TODO ОБЪЕДИНИТЬ МАППЕРЫ
        return PrivateProjectShortResponseDto.builder()
                .projectName(entity.getProjectName())
                .title(entity.getTitle())
                .authorUsername(entity.getAuthorAccount().getUsername())
                .tags(entity.getTagsAsString())
                .build();
    }
}