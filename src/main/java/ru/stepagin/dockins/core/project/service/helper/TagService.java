package ru.stepagin.dockins.core.project.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.core.project.entity.TagEntity;
import ru.stepagin.dockins.core.project.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public List<TagEntity> findOrCreateTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return List.of();
        }

        List<String> normalizedNames = tagNames.stream()
                .map(this::normalizeTag)
                .collect(Collectors.toList());

        List<TagEntity> existingTags = tagRepository.findAllByNameIn(normalizedNames);
        Map<String, TagEntity> existingTagMap = existingTags.stream()
                .collect(Collectors.toMap(TagEntity::getName, tag -> tag));

        List<TagEntity> result = new ArrayList<>();

        for (String normalized : normalizedNames) {
            TagEntity tag = existingTagMap.get(normalized);
            if (tag != null) {
                result.add(tag);
            } else {
                TagEntity newTag = TagEntity.builder().name(normalized).build();
                result.add(newTag);
            }
        }
        tagRepository.saveAll(result);
        return result;
    }

    private String normalizeTag(String raw) {
        return raw.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9-]", "");
    }
}