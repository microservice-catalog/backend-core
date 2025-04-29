package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.FavouriteStatusResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainFavouriteServicePort;
import ru.stepagin.dockins.api.v1.user.service.UserDomainFavouriteServicePort;
import ru.stepagin.dockins.core.auth.repository.AccountRepository;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectUserFavouriteEntity;
import ru.stepagin.dockins.core.project.enumeration.FavouriteStatus;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserFavouriteRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements UserDomainFavouriteServicePort, ProjectDomainFavouriteServicePort {

    private final ProjectInfoRepository projectRepository;
    private final ProjectUserFavouriteRepository projectUserFavouriteRepository;
    private final AuthServiceImpl authService;
    private final AccountRepository accountRepository;
    private final ProjectMapper projectMapper;

    @Override
    @Transactional
    public FavouriteStatusResponseDto addFavourite(String username, String projectName) {
        AccountEntity currentUser = authService.getCurrentUser();

        ProjectInfoEntity projectToLike = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        Optional<ProjectUserFavouriteEntity> existing = projectUserFavouriteRepository.findByUserAndProject(currentUser, projectToLike);

        if (existing.isPresent() && !existing.get().isDeleted()) {
            return new FavouriteStatusResponseDto(FavouriteStatus.ALREADY_SET.name());
        }

        if (existing.isPresent()) {
            ProjectUserFavouriteEntity fav = existing.get();
            fav.setDeleted(false);
            projectUserFavouriteRepository.save(fav);
        } else {
            ProjectUserFavouriteEntity newFavourite = ProjectUserFavouriteEntity.builder()
                    .user(currentUser)
                    .project(projectToLike)
                    .createdOn(java.time.LocalDateTime.now())
                    .deleted(false)
                    .build();
            projectUserFavouriteRepository.save(newFavourite);
        }

        return new FavouriteStatusResponseDto(FavouriteStatus.SUCCESSFULLY_SET.name());
    }

    @Override
    @Transactional
    public FavouriteStatusResponseDto removeFavourite(String username, String projectName) {
        AccountEntity currentUser = authService.getCurrentUser();

        ProjectInfoEntity projectToLike = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        Optional<ProjectUserFavouriteEntity> existing = projectUserFavouriteRepository.findByUserAndProject(currentUser, projectToLike);

        if (existing.isEmpty() || existing.get().isDeleted()) {
            return new FavouriteStatusResponseDto(FavouriteStatus.ALREADY_RELEASED.name());
        }

        ProjectUserFavouriteEntity fav = existing.get();
        fav.markAsDeleted();
        projectUserFavouriteRepository.save(fav);

        return new FavouriteStatusResponseDto(FavouriteStatus.SUCCESSFULLY_RELEASED.name());
    }

    @Override
    public List<PublicProjectShortResponseDto> getUserFavourites(String username) {
        AccountEntity user = accountRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ProjectNotFoundException("Пользователь не найден."));

        List<ProjectUserFavouriteEntity> favourites = projectUserFavouriteRepository.findAllByUser(user);

        return favourites.stream()
                .map(fav -> projectMapper.mapToShortDto(fav.getProject()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicProjectShortResponseDto> getCurrentUserFavourites() {
        AccountEntity user = authService.getCurrentUser();

        List<ProjectUserFavouriteEntity> favourites = projectUserFavouriteRepository.findAllByUser(user);

        return favourites.stream()
                .map(fav -> projectMapper.mapToShortDto(fav.getProject()))
                .collect(Collectors.toList());
    }

}
