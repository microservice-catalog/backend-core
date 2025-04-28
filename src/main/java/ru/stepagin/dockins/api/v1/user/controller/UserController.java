package ru.stepagin.dockins.api.v1.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.PublicProjectShortResponseDto;
import ru.stepagin.dockins.api.v1.user.dto.UserPublicProfileResponseDto;
import ru.stepagin.dockins.core.project.service.FavouriteService;
import ru.stepagin.dockins.core.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FavouriteService favouriteService;

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
}