package ru.stepagin.dockins.domain.external.dadata;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DadataValidationService {

    public boolean validateEmail(String email) {
        // Заглушка проверки email через Dadata API
        // TODO: Реализовать вызов к реальному API, когда появятся креды
        return true;
    }
}