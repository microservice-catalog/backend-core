package ru.stepagin.dockins.api.v1.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stepagin.dockins.api.v1.project.dto.FavouriteStatusResponseDto;
import ru.stepagin.dockins.core.project.service.FavouriteService;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{username}/{projectName}/favourite")
@RequiredArgsConstructor
public class FavouriteController {

    private final FavouriteService favouriteService;

    @PutMapping
    public ResponseEntity<FavouriteStatusResponseDto> addFavourite(
            @PathVariable String username,
            @PathVariable String projectName
    ) {
        FavouriteStatusResponseDto response = favouriteService.addFavourite(username, projectName);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<FavouriteStatusResponseDto> removeFavourite(
            @PathVariable String username,
            @PathVariable String projectName
    ) {
        FavouriteStatusResponseDto response = favouriteService.removeFavourite(username, projectName);
        return ResponseEntity.ok(response);
    }
}
