package ru.stepagin.dockins.api.v1.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FavouriteStatusResponseDto {

    private String status;

}
