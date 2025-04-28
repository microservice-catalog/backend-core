package ru.stepagin.dockins.api.v1.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.ProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;
import ru.stepagin.dockins.core.project.service.FavouriteService;
import ru.stepagin.dockins.core.user.service.ProfileService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final FavouriteService favouriteService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getCurrentProfile() {
        return ResponseEntity.ok(profileService.getCurrentProfile());
    }

    @PatchMapping
    public ResponseEntity<ProfileResponseDto> updateCurrentProfile(@RequestBody ProfileUpdateRequestDto requestDto) {
        return ResponseEntity.ok(profileService.updateCurrentProfile(requestDto));
    }

    @GetMapping("/favourites")
    public ResponseEntity<List<ProjectShortResponseDto>> getUserFavourites() {
        List<ProjectShortResponseDto> favourites = favouriteService.getCurrentUserFavourites();
        return ResponseEntity.ok(favourites);
    }
}