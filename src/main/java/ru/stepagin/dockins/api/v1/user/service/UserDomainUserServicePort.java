package ru.stepagin.dockins.api.v1.user.service;

import org.springframework.data.domain.PageRequest;
import ru.stepagin.dockins.api.v1.user.dto.ProfileShortDataResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;

public interface UserDomainUserServicePort {

    UserPublicProfileResponseDto getPublicProfile(String username, PageRequest pageRequest);

    ProfileShortDataResponseDto getShortUserData(String username);

    ProfileShortDataResponseDto getCurrentUserData();

}
