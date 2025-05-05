package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectUserPullEntity;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ProjectUserPullRepository extends JpaRepository<ProjectUserPullEntity, Long> {
    @Query("select count(p) from ProjectUserPullEntity p where p.project.id = :id")
//    @Cacheable(value = "projectStatsCache", key = "#projectId + '_pullsCount'", unless = "#result == null")
    long countByProjectId(@Param("id") UUID id);

    @Query("""
            select (count(p) > 0) from ProjectUserPullEntity p
            where p.user = :user
            and p.project = :project
            and p.createdOn >= :createdOn""")
    boolean existsAfter(@Param("user") AccountEntity user,
                        @Param("project") ProjectInfoEntity project,
                        @Param("createdOn") LocalDateTime createdOn);


}