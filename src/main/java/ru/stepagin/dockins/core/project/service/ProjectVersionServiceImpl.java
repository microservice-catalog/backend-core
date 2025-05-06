package ru.stepagin.dockins.core.project.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionListResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainProjectVersionServicePort;
import ru.stepagin.dockins.core.auth.exception.ActionNotAllowedException;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.common.exception.BadUpdateDataException;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.exception.VersionAlreadyExistsException;
import ru.stepagin.dockins.core.project.exception.VersionNotFoundException;
import ru.stepagin.dockins.core.project.mapper.VersionMapper;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.repository.ProjectVersionRepository;
import ru.stepagin.dockins.core.project.service.helper.DockerCommandService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectVersionServiceImpl implements ProjectDomainProjectVersionServicePort {

    private final ProjectInfoRepository projectRepository;
    private final ProjectVersionRepository projectVersionRepository;
    private final AuthServiceImpl authService;
    private final DockerCommandService dockerCommandService;
    private final VersionMapper versionMapper;

    @Override
    @Transactional
    public ProjectVersionResponseDto createVersion(
            String username,
            String projectName,
            @Valid ProjectVersionCreateRequestDto requestDto
    ) {
        ProjectInfoEntity project = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        authService.belongToCurrentUserOrThrow(project);

        boolean exists = projectVersionRepository.existsByProjectAndName(project, requestDto.getVersionName());
        if (exists) {
            throw new VersionAlreadyExistsException("Версия с таким именем уже существует.");
        }

        ProjectVersionEntity version = ProjectVersionEntity.builder()
                .project(project)
                .name(requestDto.getVersionName())
                .build();

        version.goodFieldsOrThrow();

        projectVersionRepository.save(version);

        return versionMapper.mapToVersionResponse(version);
    }

    @Override
    public ProjectVersionResponseDto getVersion(String username, String projectName, String versionName) {
        ProjectVersionEntity version = projectVersionRepository.findByProjectNameAndVersionName(username, projectName, versionName)
                .orElseThrow(VersionNotFoundException::new);

        if (version.isPrivate() || version.getProject().isPrivate()) {
            authService.belongToCurrentUserOrThrow(version);
        }

        return versionMapper.mapToVersionResponse(version);
    }

    @Override
    @Transactional
    public ProjectVersionResponseDto updateVersion(
            String username,
            String projectName,
            String versionName,
            @Valid ProjectVersionUpdateRequestDto requestDto
    ) {
        ProjectVersionEntity version = getVersionOrThrowAndCheckAuthority(username, projectName, versionName);
        if (Boolean.TRUE.equals(requestDto.getIsPrivate())) {
            if (Boolean.TRUE.equals(requestDto.getMakeVersionDefault()) || version.isDefault())
                throw new BadUpdateDataException("Версия по умолчанию не может быть приватной.");
        }

        if (requestDto.getName() != null) version.setName(requestDto.getName());
        if (requestDto.getIsPrivate() != null) version.setPrivate(requestDto.getIsPrivate());
        if (requestDto.getGithubLink() != null) version.setLinkGithub(requestDto.getGithubLink());
        if (requestDto.getDockerHubLink() != null) {
            version.setLinkDockerhub(requestDto.getDockerHubLink());
            version.setDockerCommand(dockerCommandService.generateDockerCommand(requestDto.getDockerHubLink()));
        }

        if (Boolean.TRUE.equals(requestDto.getMakeVersionDefault()))
            version.makeDefault();

        version.goodFieldsOrThrow();
        projectVersionRepository.save(version);

        return versionMapper.mapToVersionResponse(version);
    }

    @Override
    public ProjectVersionListResponseDto getAllProjectVersions(String username, String projectName) {
        ProjectInfoEntity project = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);
        try {
            authService.belongToCurrentUserOrThrow(project);
            return versionMapper.mapToVersionListResponseWithPrivate(project);
        } catch (ActionNotAllowedException e) {
            return versionMapper.mapToVersionListResponse(project);
        }
    }

    @Override
    @Transactional
    public void deleteVersion(String username, String projectName, String versionName) {
        var version = getVersionOrThrowAndCheckAuthority(username, projectName, versionName);
        if (version.isDefault())
            throw new BadUpdateDataException("Невозможно удалить версию по умолчанию.");
        version.markAsDeleted();
        projectVersionRepository.save(version);
    }

    private ProjectVersionEntity getVersionOrThrowAndCheckAuthority(String username, String projectName, String versionName) {
        ProjectVersionEntity version = projectVersionRepository.findByProjectNameAndVersionName(username, projectName, versionName)
                .orElseThrow(VersionNotFoundException::new);

        authService.belongToCurrentUserOrThrow(version);

        return version;
    }

    private ProjectVersionResponseDto mapToVersionResponse(ProjectVersionEntity version) {
        return ProjectVersionResponseDto.builder()
                .versionName(version.getName())
                .dockerHubLink(version.getLinkDockerhub())
                .githubLink(version.getLinkGithub())
                .dockerCommand(version.getDockerCommand())
                .envParameters(List.of()) // позже будут реальные параметры
                .build();
    }
}
