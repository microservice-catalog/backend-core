package ru.stepagin.dockins.api.v1.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.stepagin.dockins.api.v1.project.enumeration.FavouriteStatus;

@Getter
@Builder
@AllArgsConstructor
public class FavouriteStatusResponseDto {

    private FavouriteStatus status;

}
