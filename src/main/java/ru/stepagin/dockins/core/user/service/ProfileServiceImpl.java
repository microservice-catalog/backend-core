package ru.stepagin.dockins.core.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.user.dto.ProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;
import ru.stepagin.dockins.api.v1.user.service.UserDomainProfileServicePort;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements UserDomainProfileServicePort {

    private final AuthServiceImpl authService;
    private final ProjectInfoRepository projectInfoRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponseDto getCurrentProfile() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        AccountEntity currentUser = authService.getCurrentUser();
        Page<ProjectInfoEntity> publicProjectsPage = projectInfoRepository
                .findByAuthorAccountAndPrivateFalse(currentUser, pageRequest);
        Page<ProjectInfoEntity> privateProjectsPage = projectInfoRepository
                .findByAuthorAccountAndPrivateTrue(currentUser, pageRequest);
        return profileMapper.mapToDto(currentUser, publicProjectsPage, privateProjectsPage);
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

}