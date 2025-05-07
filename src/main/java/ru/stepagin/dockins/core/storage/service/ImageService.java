package ru.stepagin.dockins.core.storage.service;

import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.stepagin.dockins.api.v1.image.dto.ImageDto;
import ru.stepagin.dockins.api.v1.image.dto.ImageListDto;
import ru.stepagin.dockins.api.v1.image.service.UserPhotoDomainFileService;
import ru.stepagin.dockins.api.v1.image.service.UserPhotoDomainImageService;
import ru.stepagin.dockins.core.auth.exception.ActionNotAllowedException;
import ru.stepagin.dockins.core.auth.repository.AccountRepository;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.storage.entity.ImageEntity;
import ru.stepagin.dockins.core.storage.exception.BadContentTypeException;
import ru.stepagin.dockins.core.storage.exception.BadFileException;
import ru.stepagin.dockins.core.storage.exception.FileNotFoundException;
import ru.stepagin.dockins.core.storage.mapper.ImageMapper;
import ru.stepagin.dockins.core.storage.repository.ImageRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService implements UserPhotoDomainImageService, UserPhotoDomainFileService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final AuthServiceImpl authService;
    private final AccountRepository accountRepository;

    @Override
    public ImageListDto getUserPhotos(String username) {
        return new ImageListDto(imageRepository.findByUsername(username).stream()
                .map(imageMapper::convertToDto)
                .toList());
    }

    @Override
    public ImageDto getMainUserPhoto(String username) {
        var lastPhoto = imageRepository.findLastUserPhoto(username);
        if (lastPhoto.isPresent()) {
            return imageMapper.convertToDto(lastPhoto.get());
        }
        return new ImageDto();
    }

    @Override
    public byte[] getFileBytes(String photoId) {
        var photo = imageRepository.findById(UUID.fromString(photoId))
                .orElseThrow(() -> new FileNotFoundException(photoId));
        return photo.getBytes();
    }

    @Override
    @Transactional
    public ImageDto savePhoto(String username, MultipartFile file) {
        var user = authService.getCurrentUser();
        if (!Objects.equals(username, user.getUsername())) {
            throw new ActionNotAllowedException("Фотографию можно загрузить только в свой профиль.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadContentTypeException("File is not an image");
        }

        // Resize to 300x300
        try {
            InputStream in = file.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Thumbnails.of(in)
                    .size(300, 300)
                    .outputFormat("jpg")
                    .toOutputStream(baos);
            ImageEntity image = ImageEntity.builder()
                    .account(user)
                    .bytes(baos.toByteArray())
                    .contentType(contentType)
                    .name(file.getOriginalFilename())
                    .build();

            var savedImage = imageRepository.save(image);

            user.setAvatarUrl(getImageUrl(savedImage.getId().toString()));
            accountRepository.save(user);

            return imageMapper.convertToDto(savedImage);
        } catch (IOException e) {
            throw new BadFileException("Invalid image file");
        }
    }

    @Override
    @Transactional
    public void deletePhoto(String username, String photoId) {
        var user = authService.getCurrentUser();
        if (!Objects.equals(username, user.getUsername())) {
            throw new ActionNotAllowedException("Фотографию можно удалить только из своего профиля.");
        }

        var photo = imageRepository.findById(UUID.fromString(photoId))
                .orElseThrow(() -> new FileNotFoundException(photoId));
        authService.belongToCurrentUserOrThrow(photo);

        photo.markAsDeleted();

        var lastPhoto = imageRepository.findLastUserPhoto(username);
        if (lastPhoto.isPresent()) {
            user.setAvatarUrl(getImageUrl(lastPhoto.get().getId().toString()));
        } else {
            user.setAvatarUrl(null);
        }
        accountRepository.save(user);

        imageRepository.save(photo);
    }


    public String getImageUrl(String photoId) {
//        return "/photos/" + photoId;
        return photoId;
    }
}
