package ru.stepagin.dockins.api.v1.image.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stepagin.dockins.api.v1.image.dto.ImageDto;
import ru.stepagin.dockins.api.v1.image.dto.ImageListDto;
import ru.stepagin.dockins.api.v1.image.service.UserPhotoDomainImageService;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/{username}/photos")
@RequiredArgsConstructor
public class UserPhotoController {

    private final UserPhotoDomainImageService imageService;

    @GetMapping
    public ResponseEntity<ImageListDto> getUserPhotos(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok(imageService.getUserPhotos(username));
    }

    @GetMapping("/main")
    public ResponseEntity<ImageDto> getMainUserPhoto(
            @PathVariable("username") String username
    ) {
        return ResponseEntity.ok(imageService.getMainUserPhoto(username));
    }

    @PostMapping
    public ResponseEntity<ImageDto> uploadPhoto(
            @PathVariable("username") String username,
            @RequestParam("photo") MultipartFile photo
    ) {
        return ResponseEntity.ok(imageService.savePhoto(username, photo));
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable("username") String username,
            @PathVariable("photoId") String photoId
    ) {
        imageService.deletePhoto(username, photoId);
        return ResponseEntity.noContent().build();
    }
}
