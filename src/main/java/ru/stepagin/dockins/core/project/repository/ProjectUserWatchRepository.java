package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectUserWatchEntity;

import java.util.UUID;

@Repository
public interface ProjectUserWatchRepository extends JpaRepository<ProjectUserWatchEntity, Long> {
    @Query("select count(p) from ProjectUserWatchEntity p where p.project.id = :id")
//    @Cacheable(value = "projectStatsCache", key = "#projectId + '_watchesCount'", unless = "#result == null")
    long countByProjectId(@Param("id") UUID id);

    @Query("select count(p) from ProjectUserWatchEntity p where p.user.id = :id")
//    @Cacheable(value = "projectUserStatsCache", key = "#userId + '_watchesCount'", unless = "#result == null")
    long countByAccountId(@Param("id") UUID id);

}

