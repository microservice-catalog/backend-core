package ru.stepagin.dockins.external.dadata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DadataValidationService {

    public void validateEmail(String email) {
        // Заглушка проверки email через Dadata API
        // TODO: Реализовать вызов к реальному API, когда появятся креды
    }
}