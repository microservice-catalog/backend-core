package ru.stepagin.dockins.api.v1.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;
import ru.stepagin.dockins.api.v1.user.service.UserDomainFavouriteServicePort;
import ru.stepagin.dockins.api.v1.user.service.UserDomainProfileServicePort;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserDomainProfileServicePort profileService;
    private final UserDomainFavouriteServicePort favouriteService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getCurrentProfile() {
        return ResponseEntity.ok(profileService.getCurrentProfile());
    }

    @PatchMapping
    public ResponseEntity<Void> updateCurrentProfile(@RequestBody ProfileUpdateRequestDto requestDto) {
        profileService.updateCurrentProfile(requestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favourites")
    public ResponseEntity<List<PublicProjectShortResponseDto>> getUserFavourites() {
        List<PublicProjectShortResponseDto> favourites = favouriteService.getCurrentUserFavourites();
        return ResponseEntity.ok(favourites);
    }

}