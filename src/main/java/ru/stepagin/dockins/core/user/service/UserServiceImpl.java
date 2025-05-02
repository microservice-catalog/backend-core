package ru.stepagin.dockins.core.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.user.dto.ProfileShortDataResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.service.UserDomainUserServicePort;
import ru.stepagin.dockins.core.auth.repository.AccountRepository;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;
import ru.stepagin.dockins.core.user.exception.UserNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDomainUserServicePort {

    private final AccountRepository accountRepository;
    private final ProjectInfoRepository projectRepository;
    private final ProfileMapper profileMapper;
    private final AuthServiceImpl authServiceImpl;

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

}