package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectVersionService;
import ru.stepagin.dockins.core.auth.service.AuthService;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.exception.ProjectNotFoundException;
import ru.stepagin.dockins.core.project.exception.VersionAlreadyExistsException;
import ru.stepagin.dockins.core.project.exception.VersionNotFoundException;
import ru.stepagin.dockins.core.project.repository.ProjectInfoRepository;
import ru.stepagin.dockins.core.project.repository.ProjectVersionRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectVersionServiceImpl implements ProjectVersionService {

    private final ProjectInfoRepository projectRepository;
    private final ProjectVersionRepository projectVersionRepository;
    private final AuthService authService;

    @Override
    public ProjectVersionResponseDto createVersion(String projectName, ProjectVersionCreateRequestDto requestDto) {
        AccountEntity currentUser = authService.getCurrentUser();

        ProjectInfoEntity project = projectRepository.findByAuthorAccountAndProjectName(currentUser.getUsername(), projectName)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден."));

        boolean exists = projectVersionRepository.existsByProjectAndName(project, requestDto.getVersionName());
        if (exists) {
            throw new VersionAlreadyExistsException("Версия с таким именем уже существует.");
        }

        ProjectVersionEntity version = ProjectVersionEntity.builder()
                .project(project)
                .name(requestDto.getVersionName())
                .createdOn(java.time.LocalDateTime.now())
                .build();

        projectVersionRepository.save(version);

        return mapToVersionResponse(version);
    }

    @Override
    public ProjectVersionResponseDto getVersion(String projectName, String versionName) {
        ProjectVersionEntity version = projectVersionRepository.findByProjectNameAndVersionName(projectName, versionName)
                .orElseThrow(() -> new VersionNotFoundException("Версия проекта не найдена."));

        return mapToVersionResponse(version);
    }

    @Override
    public ProjectVersionResponseDto getDefaultProjectVersion(String projectName, String versionName) {
        ProjectVersionEntity version = projectVersionRepository.findByProjectNameAndVersionName(projectName, versionName)
                .orElseThrow(() -> new VersionNotFoundException("Версия проекта не найдена."));

        return mapToVersionResponse(version);
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
