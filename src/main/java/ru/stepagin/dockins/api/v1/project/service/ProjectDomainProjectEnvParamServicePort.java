package ru.stepagin.dockins.api.v1.project.service;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamUpdateRequestDto;

public interface ProjectDomainProjectEnvParamServicePort {

    @Transactional
    EnvParamDto addEnvParam(String username, String projectName, String versionName, @Valid EnvParamCreateRequestDto dto);

    @Transactional
    EnvParamDto updateEnvParam(String username, String projectName, String versionName, String paramName, @Valid EnvParamUpdateRequestDto dto);

    @Transactional
    void deleteEnvParam(String username, String projectName, String versionName, String paramName);

}