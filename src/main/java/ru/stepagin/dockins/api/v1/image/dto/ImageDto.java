package ru.stepagin.dockins.api.v1.image.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private String id;

    private String name;

    private String created;

    private Long size;

    private String url;

}
