package ru.stepagin.dockins.api.v1.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;

import java.util.List;

@Getter
@Setter
@Builder
public class UserPublicProfileResponseDto {

    private String username;

    private String fullName;

    private String description;

    private String avatarUrl;

    private List<PublicProjectShortResponseDto> publicProjects;

    private int favouritesCount;

    private int viewsCount;

    private int likesCount;

}