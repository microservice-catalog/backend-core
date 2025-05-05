package ru.stepagin.dockins.api.v1.user.service;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;

public interface UserDomainProfileServicePort {

    @Transactional
    void updateProfile(String username, @Valid ProfileUpdateRequestDto dto);

}
