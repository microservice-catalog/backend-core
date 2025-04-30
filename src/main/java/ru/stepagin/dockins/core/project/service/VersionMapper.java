package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionListResponseDto;
import ru.stepagin.dockins.api.v1.project.dto.ProjectVersionResponseDto;
import ru.stepagin.dockins.core.project.entity.ProjectEnvParamEntity;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.repository.ProjectEnvParamRepository;
import ru.stepagin.dockins.core.project.repository.ProjectVersionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VersionMapper {
    private final ProjectVersionRepository projectVersionRepository;
    private final ProjectEnvParamRepository envParamRepository;
    private final EnvMapper envMapper;

    public ProjectVersionResponseDto mapToVersionResponse(ProjectVersionEntity version) {
        List<ProjectEnvParamEntity> envParams = envParamRepository.findByProjectVersion(version);

        return ProjectVersionResponseDto.builder()
                .versionName(version.getName())
                .dockerHubLink(version.getLinkDockerhub())
                .githubLink(version.getLinkGithub())
                .dockerCommand(version.getDockerCommand())
                .envParameters(envParams.stream()
                        .map(envMapper::convertToDto)
                        .collect(Collectors.toList())
                )
                .isPrivate(version.isPrivate())
                .build();
    }

    public ProjectVersionListResponseDto mapToVersionListResponse(ProjectInfoEntity project) {
        List<ProjectVersionEntity> versions = projectVersionRepository.findByProjectIdAndPrivateFalse(project.getId());

        return ProjectVersionListResponseDto.builder()
                .defaultVersionName(project.getDefaultProjectVersion().getName())
                .versions(versions.stream()
                        .map(this::mapToVersionResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public ProjectVersionListResponseDto mapToVersionListResponseWithPrivate(ProjectInfoEntity project) {
        List<ProjectVersionEntity> versions = projectVersionRepository.findByProjectId(project.getId());

        return ProjectVersionListResponseDto.builder()
                .defaultVersionName(project.getDefaultProjectVersion().getName())
                .versions(versions.stream()
                        .map(this::mapToVersionResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
