package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectUserPullEntity;

import java.util.UUID;

@Repository
public interface ProjectUserPullRepository extends JpaRepository<ProjectUserPullEntity, Long> {
    @Query("select count(p) from ProjectUserPullEntity p where p.project.id = :id")
//    @Cacheable(value = "projectStatsCache", key = "#projectId + '_pullsCount'", unless = "#result == null")
    long countByProjectId(@Param("id") UUID id);

}