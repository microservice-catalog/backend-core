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

    private static EnvParamDto convertToDto(ProjectEnvParamEntity entity) {
        return EnvParamDto.builder()
                .name(entity.getName())
                .required(entity.getRequired())
                .defaultValue(entity.getDefaultValue())
                .build();
    }

    @Override
    @Transactional
    public EnvParamDto addEnvParam(String projectName, String versionName, @Valid EnvParamCreateRequestDto dto) {
        String username = authService.getCurrentUser().getUsername();
        ProjectVersionEntity version = findProjectVersion(username, projectName, versionName);

        if (projectEnvParamRepository.existsByProjectVersionAndName(version, dto.getName())) {
            // todo проверка на deleted=true
            throw new EnvParamAlreadyExistsException("Переменная окружения с таким именем уже существует.");
        }

        ProjectEnvParamEntity param = ProjectEnvParamEntity.builder()
                .projectVersion(version)
                .name(dto.getName())
                .required(dto.getRequired())
                .defaultValue(dto.getDefaultValue())
                .createdOn(LocalDateTime.now())
                .build();

        log.info("Пользователь '{}' добавил параметр '{}' в версию '{}'", authService.getCurrentUser().getUsername(), dto.getName(), versionName);

        return convertToDto(projectEnvParamRepository.save(param));
    }

    @Override
    @Transactional
    public EnvParamDto updateEnvParam(
            String projectName,
            String versionName,
            String paramName,
            @Valid EnvParamUpdateRequestDto dto
    ) {
        String username = authService.getCurrentUser().getUsername();
        ProjectEnvParamEntity param = findEnvParam(username, projectName, versionName, paramName);

        if (dto.getRequired() != null) {
            param.setRequired(dto.getRequired());
        }
        if (dto.getDefaultValue() != null) {
            param.setDefaultValue(dto.getDefaultValue());
        }

        param.setUpdatedOn(LocalDateTime.now());

        log.info("Пользователь '{}' обновил параметр '{}' в версии '{}'", authService.getCurrentUser().getUsername(), paramName, versionName);

        return convertToDto(projectEnvParamRepository.save(param));
    }

    @Override
    @Transactional
    public void deleteEnvParam(String projectName, String versionName, String paramName) {
        String username = authService.getCurrentUser().getUsername();
        ProjectEnvParamEntity param = findEnvParam(username, projectName, versionName, paramName);

        param.markAsDeleted();
        projectEnvParamRepository.save(param);

        log.info("Пользователь '{}' удалил параметр '{}' в версии '{}'", authService.getCurrentUser().getUsername(), paramName, versionName);
    }

    private ProjectVersionEntity findProjectVersion(String username, String projectName, String versionName) {
        // todo проверка на deleted
        return projectVersionRepository.findByProjectNameAndVersionName(username, projectName, versionName)
                .orElseThrow(() -> new VersionNotFoundException("Версия проекта не найдена."));
    }

    private ProjectEnvParamEntity findEnvParam(String username, String projectName, String versionName, String paramName) {
        ProjectVersionEntity version = findProjectVersion(username, projectName, versionName);

        return projectEnvParamRepository.findByProjectVersionAndName(version, paramName)
                .orElseThrow(() -> new EnvParamNotFoundException("Параметр окружения не найден."));
    }
}
