package ru.stepagin.dockins.core.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.core.auth.service.AuthServiceImpl;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectUserPullEntity;
import ru.stepagin.dockins.core.project.entity.ProjectUserWatchEntity;
import ru.stepagin.dockins.core.project.repository.ProjectUserPullRepository;
import ru.stepagin.dockins.core.project.repository.ProjectUserWatchRepository;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectStatsService {
    private final ProjectUserWatchRepository projectWatchRepository;
    private final ProjectUserPullRepository projectPullRepository;
    private final AuthServiceImpl authService;
    private final long watchPeriodInMinutes = 1;
    private final long pullPeriodInMinutes = 1;

    @Transactional
    public void recordWatch(ProjectInfoEntity project) {
        var u = authService.getCurrentUserOrNull();
        if (u == null) return;
        if (project.getAuthorAccount().getUsername().equals(u.getUsername())) return;
        if (projectWatchRepository.existsAfter(u, project, LocalDateTime.now().minusMinutes(watchPeriodInMinutes)))
            return;
        projectWatchRepository.save(ProjectUserWatchEntity.builder()
                .user(u)
                .project(project)
                .build());
    }

    @Transactional
    public void recordPull(ProjectInfoEntity project) {
        var u = authService.getCurrentUserOrNull();
        if (u == null) return;
        if (project.getAuthorAccount().getUsername().equals(u.getUsername())) return;
        if (projectPullRepository.existsAfter(u, project, LocalDateTime.now().minusMinutes(pullPeriodInMinutes)))
            return;
        projectPullRepository.save(ProjectUserPullEntity.builder()
                .user(u)
                .project(project)
                .build());
    }
}
