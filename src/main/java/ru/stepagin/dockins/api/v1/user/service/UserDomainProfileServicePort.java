package ru.stepagin.dockins.api.v1.user.service;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.user.dto.ProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;

public interface UserDomainProfileServicePort {

    ProfileResponseDto getCurrentProfile();

    @Transactional
    void updateCurrentProfile(@Valid ProfileUpdateRequestDto dto);

}
