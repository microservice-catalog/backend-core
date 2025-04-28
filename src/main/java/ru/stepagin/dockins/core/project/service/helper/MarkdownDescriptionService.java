package ru.stepagin.dockins.core.project.service.helper;

import org.springframework.stereotype.Service;

@Service
public class MarkdownDescriptionService {

    public String generateDefaultDescription() {
        return "## Описание микросервиса\n\n(Автоматически сгенерировано)"; // todo генерация
    }
}