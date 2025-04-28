package ru.stepagin.dockins.api.v1.project.service;

import ru.stepagin.dockins.api.v1.project.dto.EnvParamCreateRequestDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamDto;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamUpdateRequestDto;

public interface ProjectEnvParamService {
    EnvParamDto addEnvParam(String projectName, String versionName, EnvParamCreateRequestDto dto);

    EnvParamDto updateEnvParam(String projectName, String versionName, String paramName, EnvParamUpdateRequestDto dto);

    void deleteEnvParam(String projectName, String versionName, String paramName);
}