package ru.stepagin.dockins.core.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.service.UserDomainUserServicePort;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.core.user.exception.UserNotFoundException;
import ru.stepagin.dockins.core.user.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDomainUserServicePort {

    private final AccountRepository accountRepository;
    private final ProjectInfoRepository projectRepository;

    @Override
    public UserPublicProfileResponseDto getPublicProfile(String username, PageRequest pageRequest) {
        AccountEntity user = accountRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден."));

        Page<ProjectInfoEntity> projects = projectRepository.findByAuthorAccountAndPrivateFalse(user, pageRequest);

        return UserPublicProfileResponseDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .description(user.getDescription())
                .avatarUrl(user.getAvatarUrl())
                .publicProjects(projects.stream().map(this::mapToShortDto).collect(Collectors.toList()))
                .favouritesCount(0) // todo
                .viewsCount(0) // todo
                .likesCount(0) // todo
                .build();
    }

    private PublicProjectShortResponseDto mapToShortDto(ProjectInfoEntity entity) {
        return PublicProjectShortResponseDto.builder()
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