package ru.stepagin.dockins.api.v1.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileShortDataResponseDto {

    private String username;

    private String fullName;

    private String avatarUrl;

}
