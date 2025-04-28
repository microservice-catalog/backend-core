package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.FavouriteStatusResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.enumeration.FavouriteStatus;
import ru.stepagin.dockins.core.auth.service.AuthService;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectUserFavouriteEntity;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.core.user.repository.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavouriteService {

    private final ProjectInfoRepository projectRepository;
    private final ProjectUserFavouriteRepository favouriteRepository;
    private final AuthService authService;
    private final AccountRepository accountRepository;

    public FavouriteStatusResponseDto addFavourite(String username, String projectName) {
        AccountEntity currentUser = authService.getCurrentUser();

        ProjectInfoEntity project = projectRepository.findByAuthorAccountAndProjectName(username, projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден."));

        Optional<ProjectUserFavouriteEntity> existing = favouriteRepository.findByUserAndProject(currentUser, project);

        if (existing.isPresent() && !existing.get().getDeleted()) {
            return new FavouriteStatusResponseDto(FavouriteStatus.ALREADY_SET);
        }

        if (existing.isPresent()) {
            ProjectUserFavouriteEntity fav = existing.get();
            fav.setDeleted(false);
            favouriteRepository.save(fav);
        } else {
            ProjectUserFavouriteEntity newFavourite = ProjectUserFavouriteEntity.builder()
                    .user(currentUser)
                    .project(project)
                    .createdOn(java.time.LocalDateTime.now())
                    .deleted(false)
                    .build();
            favouriteRepository.save(newFavourite);
        }

        return new FavouriteStatusResponseDto(FavouriteStatus.SUCCESSFULLY_SET);
    }

    public FavouriteStatusResponseDto removeFavourite(String username, String projectName) {
        AccountEntity currentUser = authService.getCurrentUser();

        ProjectInfoEntity project = projectRepository.findByAuthorAccountAndProjectName(username, projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден."));

        Optional<ProjectUserFavouriteEntity> existing = favouriteRepository.findByUserAndProject(currentUser, project);

        if (existing.isEmpty() || existing.get().getDeleted()) {
            return new FavouriteStatusResponseDto(FavouriteStatus.ALREADY_RELEASED);
        }

        ProjectUserFavouriteEntity fav = existing.get();
        fav.setDeleted(true);
        fav.setDeletedOn(java.time.LocalDateTime.now());
        favouriteRepository.save(fav);

        return new FavouriteStatusResponseDto(FavouriteStatus.SUCCESSFULLY_RELEASED);
    }

    public List<ProjectShortResponseDto> getUserFavourites(String username) {
        AccountEntity user = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ProjectNotFoundException("Пользователь не найден."));

        List<ProjectUserFavouriteEntity> favourites = favouriteRepository.findAllByUser(user);

        return favourites.stream()
                .map(fav -> mapToShortDto(fav.getProject()))
                .collect(Collectors.toList());
    }

    public List<ProjectShortResponseDto> getCurrentUserFavourites() {
        AccountEntity user = authService.getCurrentUser();

        List<ProjectUserFavouriteEntity> favourites = favouriteRepository.findAllByUser(user);

        return favourites.stream()
                .map(fav -> mapToShortDto(fav.getProject()))
                .collect(Collectors.toList());
    }

    private ProjectShortResponseDto mapToShortDto(ProjectInfoEntity entity) {
        return ProjectShortResponseDto.builder()
                .projectName(entity.getProjectName())
                .title(entity.getTitle())
                .authorUsername(entity.getAuthorAccount().getUsername())
                .likesCount(0)
                .downloadsCount(0)
                .viewsCount(0)
                .tags(List.of())
                .build();
    }
}
