package ru.stepagin.dockins.api.v1.image.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.stepagin.dockins.api.v1.image.service.UserPhotoDomainFileService;

@Slf4j
@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
public class PhotosController {

    private final UserPhotoDomainFileService fileService;

    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getMainUserPhoto(
            @PathVariable("photoId") String photoId
    ) {
        byte[] data = fileService.getFileBytes(photoId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return ResponseEntity.ok().headers(headers).body(data);
    }
}
