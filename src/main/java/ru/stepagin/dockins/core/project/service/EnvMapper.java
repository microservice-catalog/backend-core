package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.project.dto.EnvParamDto;
import ru.stepagin.dockins.core.project.entity.ProjectEnvParamEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnvMapper {

    public EnvParamDto convertToDto(ProjectEnvParamEntity entity) {
        return EnvParamDto.builder()
                .name(entity.getName())
                .required(entity.isRequired())
                .defaultValue(entity.getDefaultValue())
                .build();
    }

}
