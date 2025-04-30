package ru.stepagin.dockins.core.project.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamUpdateRequestDto;
import ru.stepagin.dockins.api.v1.project.service.ProjectDomainProjectEnvParamServicePort;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectEnvParamEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;
import ru.stepagin.dockins.core.project.exception.EnvParamAlreadyExistsException;
import ru.stepagin.dockins.core.project.exception.EnvParamNotFoundException;
import ru.stepagin.dockins.core.project.exception.VersionNotFoundException;
import ru.stepagin.dockins.core.project.repository.ProjectEnvParamRepository;
import ru.stepagin.dockins.core.project.repository.ProjectVersionRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectEnvParamServiceImpl implements ProjectDomainProjectEnvParamServicePort {

    private final ProjectVersionRepository projectVersionRepository;
    private final ProjectEnvParamRepository projectEnvParamRepository;
    private final AuthServiceImpl authService;
    private final EnvMapper envMapper;

    @Override
    @Transactional
    public EnvParamDto addEnvParam(String username, String projectName, String versionName, @Valid EnvParamCreateRequestDto dto) {
        ProjectVersionEntity version = findProjectVersion(username, projectName, versionName);

        authService.belongToCurrentUserOrThrow(version);

        ProjectEnvParamEntity param = projectEnvParamRepository.findByProjectVersionAndNameWithDeleted(version, dto.getName())
                .orElse(null);

        if (param != null) {
            if (!param.isDeleted()) {
                throw new EnvParamAlreadyExistsException("Переменная окружения с таким именем уже существует.");
            }
            param.markAsRestored();
            param.setRequired(dto.isRequired());
            param.setDefaultValue(dto.getDefaultValue());
        } else {
            param = ProjectEnvParamEntity.builder()
                    .projectVersion(version)
                    .name(dto.getName())
                    .required(dto.isRequired())
                    .defaultValue(dto.getDefaultValue())
                    .createdOn(LocalDateTime.now())
                    .build();
        }
        param.goodFieldsOrThrow();
        log.info("Пользователь '{}' добавил параметр '{}' в версию '{}' проекта '{}'",
                authService.getCurrentUser().getUsername(),
                dto.getName(),
                versionName,
                version.getProject().getProjectName());

        return envMapper.convertToDto(projectEnvParamRepository.save(param));
    }

    @Override
    @Transactional
    public EnvParamDto updateEnvParam(
            String username,
            String projectName,
            String versionName,
            String paramName,
            @Valid EnvParamUpdateRequestDto dto
    ) {
        ProjectVersionEntity projectVersion = findProjectVersion(username, projectName, versionName);
        authService.belongToCurrentUserOrThrow(projectVersion);

        ProjectEnvParamEntity param = findEnvParamOrThrow(username, projectName, versionName, paramName, true);

        if (dto.getRequired() != null) {
            param.setRequired(dto.getRequired());
        }
        if (dto.getDefaultValue() != null) {
            param.setDefaultValue(dto.getDefaultValue());
        }

        param.setUpdatedOn(LocalDateTime.now());

        log.info("Пользователь '{}' обновил параметр '{}' в версии '{}'", authService.getCurrentUser().getUsername(), paramName, versionName);

        return envMapper.convertToDto(projectEnvParamRepository.save(param));
    }

    @Override
    @Transactional
    public void deleteEnvParam(String username, String projectName, String versionName, String paramName) {
        ProjectEnvParamEntity param = findEnvParamOrThrow(username, projectName, versionName, paramName, true);

        authService.belongToCurrentUserOrThrow(param.getProjectVersion());

        param.markAsDeleted();
        projectEnvParamRepository.save(param);

        log.info("Пользователь '{}' удалил параметр '{}' в версии '{}'", authService.getCurrentUser().getUsername(), paramName, versionName);
    }

    private ProjectVersionEntity findProjectVersion(String username, String projectName, String versionName) {
        // todo проверка на deleted
        return projectVersionRepository.findByProjectNameAndVersionName(username, projectName, versionName)
                .orElseThrow(() -> new VersionNotFoundException("Версия проекта не найдена."));
    }

    private ProjectEnvParamEntity findEnvParamOrThrow(
            String username,
            String projectName,
            String versionName,
            String paramName,
            boolean throwIfDeleted
    ) {
        ProjectVersionEntity version = findProjectVersion(username, projectName, versionName);
        var param = projectEnvParamRepository.findByProjectVersionAndNameWithDeleted(version, paramName)
                .orElseThrow(() -> new EnvParamNotFoundException("Параметр окружения не найден."));
        if (throwIfDeleted && param.isDeleted()) {
            throw new EnvParamNotFoundException("Параметр окружения не найден.");
        }
        return param;
    }
}
