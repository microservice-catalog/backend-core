package ru.stepagin.dockins.api.v1.project.service;

import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.api.v1.project.dto.FavouriteStatusResponseDto;

public interface ProjectDomainFavouriteServicePort {

    @Transactional
    FavouriteStatusResponseDto addFavourite(String username, String projectName);

    @Transactional
    FavouriteStatusResponseDto removeFavourite(String username, String projectName);

}
