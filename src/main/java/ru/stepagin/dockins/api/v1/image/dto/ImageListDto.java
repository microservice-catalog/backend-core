package ru.stepagin.dockins.api.v1.image.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageListDto {
    private List<ImageDto> images;
}
