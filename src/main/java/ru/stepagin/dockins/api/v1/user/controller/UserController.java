package ru.stepagin.dockins.api.v1.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileShortDataResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.ProfileUpdateRequestDto;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;
import ru.stepagin.dockins.api.v1.user.service.UserDomainFavouriteServicePort;
import ru.stepagin.dockins.api.v1.user.service.UserDomainProfileServicePort;
import ru.stepagin.dockins.api.v1.user.service.UserDomainUserServicePort;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDomainUserServicePort userService;
    private final UserDomainProfileServicePort profileService;
    private final UserDomainFavouriteServicePort favouriteService;

    @GetMapping("/me")
    public ResponseEntity<ProfileShortDataResponseDto> getPublicProfile(
    ) {
        return ResponseEntity.ok(userService.getCurrentUserData());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserPublicProfileResponseDto> getPublicProfile(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @PathVariable String username
    ) {
        return ResponseEntity.ok(userService.getPublicProfile(username, PageRequest.of(page, limit)));
    }

    @GetMapping("/{username}/favourites")
    public ResponseEntity<List<PublicProjectShortResponseDto>> getUserFavourites(@PathVariable String username) {
        List<PublicProjectShortResponseDto> favourites = favouriteService.getUserFavourites(username);
        return ResponseEntity.ok(favourites);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<Void> updateProfile(
            @PathVariable String username,
            @RequestBody ProfileUpdateRequestDto requestDto
    ) {
        profileService.updateProfile(username, requestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/short")
    public ResponseEntity<ProfileShortDataResponseDto> getHeaderData(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(userService.getShortUserData(username));
    }
}