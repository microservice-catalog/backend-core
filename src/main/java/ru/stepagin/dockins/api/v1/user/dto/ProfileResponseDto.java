package ru.stepagin.dockins.api.v1.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.stepagin.dockins.api.v1.project.dto.ProjectShortResponseDto;

import java.util.List;

@Getter
@Setter
@Builder
public class ProfileResponseDto {

    private String username;

    private String fullName;

    private String description;

    private String avatarUrl;

    private List<ProjectShortResponseDto> publicProjects;

    private long favouritesCount;

    private long viewsCount;

    private long likesCount;
}
