package ru.stepagin.dockins.core.project.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainProjectVersionServicePort;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.exception.VersionAlreadyExistsException;
import ru.stepagin.dockins.core.project.exception.VersionNotFoundException;
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

    @Override
    @Transactional
    public ProjectVersionResponseDto createVersion(
            String username,
            String projectName,
            @Valid ProjectVersionCreateRequestDto requestDto
    ) {
        ProjectInfoEntity project = getProjectAndCheckAuthority(username, projectName);

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

        return mapToVersionResponse(version);
    }

    @Override
    public ProjectVersionResponseDto getVersion(String username, String projectName, String versionName) {
        ProjectVersionEntity version = projectVersionRepository.findByProjectNameAndVersionName(username, projectName, versionName)
                .orElseThrow(VersionNotFoundException::new);

        if (version.isPrivate()) {
            authService.belongToCurrentUserOrThrow(version);
        }

        return mapToVersionResponse(version);
    }

    @Override
    @Transactional
    public ProjectVersionResponseDto updateVersion(String username, String projectName, String versionName, ProjectVersionUpdateRequestDto requestDto) {
        ProjectVersionEntity version = getVersionOrThrowAndCheckAuthority(username, projectName, versionName);

        if (requestDto.getName() != null) version.setName(requestDto.getName());
        if (requestDto.getIsPrivate() != null) version.setPrivate(requestDto.getIsPrivate());
        if (requestDto.getGithubLink() != null) version.setLinkGithub(requestDto.getGithubLink());
        if (requestDto.getDockerHubLink() != null) {
            version.setLinkDockerhub(requestDto.getDockerHubLink());
            version.setDockerCommand(dockerCommandService.generateDockerCommand(requestDto.getDockerHubLink()));
        }

        version.goodFieldsOrThrow();
        projectVersionRepository.save(version);

        return mapToVersionResponse(version);
    }

    @Override
    public ProjectVersionResponseDto getDefaultProjectVersion(String username, String projectName) {
        ProjectVersionEntity version = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(VersionNotFoundException::new)
                .getDefaultProjectVersion();

        return mapToVersionResponse(version);
    }

    @Override
    @Transactional
    public void deleteVersion(String username, String projectName, String versionName) {
        var version = getVersionOrThrowAndCheckAuthority(username, projectName, versionName);
        version.markAsDeleted();
        projectVersionRepository.save(version);
    }

    private ProjectInfoEntity getProjectAndCheckAuthority(String username, String projectName) {
        ProjectInfoEntity project = projectRepository.findByUsernameAndProjectName(username, projectName)
                .orElseThrow(ProjectNotFoundException::new);

        authService.belongToCurrentUserOrThrow(project);

        return project;
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
