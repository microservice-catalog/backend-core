package ru.stepagin.dockins.api.v1.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stepagin.dockins.api.v1.project.dto.ProjectShortResponseDto;
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
            @PathVariable String username
    ) {
        return ResponseEntity.ok(userService.getPublicProfile(username));
    }

    @GetMapping("/{username}/favourites")
    public ResponseEntity<List<ProjectShortResponseDto>> getUserFavourites(@PathVariable String username) {
        List<ProjectShortResponseDto> favourites = favouriteService.getUserFavourites(username);
        return ResponseEntity.ok(favourites);
    }
}