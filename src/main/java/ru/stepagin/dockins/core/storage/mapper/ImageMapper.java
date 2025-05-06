package ru.stepagin.dockins.core.storage.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.api.v1.image.dto.ImageDto;
import ru.stepagin.dockins.core.storage.entity.ImageEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageMapper {

    public ImageDto convertToDto(ImageEntity image) {
        return ImageDto.builder()
                .name(image.getName())
                .id(image.getId().toString())
                .size(image.getSize())
                .created(image.getCreatedOn().toString())
                .build();
    }

}
