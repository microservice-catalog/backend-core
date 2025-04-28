package ru.stepagin.dockins.core.project.service.helper;

import org.springframework.stereotype.Service;

@Service
public class DockerCommandService {

    public String generateDockerCommand(String dockerHubLink) {
        if (dockerHubLink == null || dockerHubLink.isBlank()) {
            return null;
        }
        return "docker run --rm " + dockerHubLink; // todo: умное создание
    }
}
