package ru.stepagin.dockins.api.v1.user.service;

import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;

import java.util.List;

public interface UserDomainFavouriteServicePort {

    List<PublicProjectShortResponseDto> getUserFavourites(String username);

}
