package ru.stepagin.dockins.core.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.user.dto.ProfileShortDataResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.service.UserDomainProfileServicePort;
import ru.stepagin.dockins.api.v1.user.service.UserDomainUserServicePort;
import ru.stepagin.dockins.core.auth.exception.ActionNotAllowedException;
import ru.stepagin.dockins.core.auth.repository.AccountRepository;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.core.user.exception.UserNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDomainUserServicePort, UserDomainProfileServicePort {

    private final AccountRepository accountRepository;
    private final ProjectInfoRepository projectRepository;
    private final ProfileMapper profileMapper;
    private final AuthServiceImpl authServiceImpl;
    private final AuthServiceImpl authService;
    private final ProjectInfoRepository projectInfoRepository;

    @Override
    public UserPublicProfileResponseDto getPublicProfile(String username, PageRequest pageRequest) {
        AccountEntity user = accountRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден."));

        Page<ProjectInfoEntity> projects = projectRepository.findByAuthorAccountAndPrivateFalse(user, pageRequest);

        return profileMapper.mapToDto(user, projects);
    }

    @Override
    public ProfileShortDataResponseDto getCurrentUserData() {
        var me = authServiceImpl.getCurrentUser();
        return ProfileShortDataResponseDto.builder()
                .avatarUrl(me.getAvatarUrl())
                .fullName(me.getFullName())
                .username(me.getUsername())
                .build();
    }

    @Override
    public ProfileShortDataResponseDto getShortUserData(String username) {
        AccountEntity user = accountRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден."));
        return ProfileShortDataResponseDto.builder()
                .avatarUrl(user.getAvatarUrl())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .build();
    }

    @Transactional
    @Override
    public void updateProfile(String username, @Valid ProfileUpdateRequestDto dto) {
        AccountEntity currentUser = authService.getCurrentUser();

        if (!currentUser.getUsername().equalsIgnoreCase(username)) {
            throw new ActionNotAllowedException();
        }

        if (dto.getFullName() != null)
            currentUser.setFullName(dto.getFullName().trim());
        if (dto.getDescription() != null)
            currentUser.setDescription(dto.getDescription().trim());

        authService.updateCurrentUserData(currentUser);
    }


}