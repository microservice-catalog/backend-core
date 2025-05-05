package ru.stepagin.dockins.api.v1.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class TagsDto {
    @NotNull
    private List<String> tags;
}
