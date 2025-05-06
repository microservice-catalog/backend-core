package ru.stepagin.dockins.api.v1.image.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.stepagin.dockins.api.v1.image.dto.ImageDto;
import ru.stepagin.dockins.api.v1.image.dto.ImageListDto;

public interface UserPhotoDomainImageService {

    ImageListDto getUserPhotos(String username);

    ImageDto getMainUserPhoto(String username);

    @Transactional
    ImageDto savePhoto(String username, MultipartFile photo);

    @Transactional
    void deletePhoto(String username, String photoId);

}
