package ru.stepagin.dockins.core.project.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectUserFavouriteEntity;

import java.util.UUID;

@Repository
public interface ProjectUserFavouriteRepository extends JpaRepository<ProjectUserFavouriteEntity, Long> {
    @Query("select count(p) from ProjectUserFavouriteEntity p where p.project.id = :id and p.deleted = false")
    @Cacheable(value = "projectStatsCache", key = "#projectId + '_favouritesCount'", unless = "#result == null")
    long countByProjectId(@Param("id") UUID id);

}
