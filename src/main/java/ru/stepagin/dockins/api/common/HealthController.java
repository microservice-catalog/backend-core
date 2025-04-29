package ru.stepagin.dockins.api.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@Validated
@RequiredArgsConstructor
public class HealthController {

    @GetMapping({"/health", "/api/v1/health"})
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    @GetMapping({"/ping", "/api/v1/ping"})
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}


