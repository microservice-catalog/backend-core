package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectUserFavouriteEntity;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectUserFavouriteRepository extends JpaRepository<ProjectUserFavouriteEntity, Long> {
    @Query("""
            select count(p) from ProjectUserFavouriteEntity p
            where p.project.id = :id
            and p.user <> p.project.authorAccount
            and p.deleted = false""")
//    @Cacheable(value = "projectStatsCache", key = "#projectId + '_favouritesCount'", unless = "#result == null")
    long countByProjectId(@Param("id") UUID id);

    @Query("""
            select count(p) from ProjectUserFavouriteEntity p
            where p.user.id = :id
            and p.user <> p.project.authorAccount
            and p.project.isPrivate = false
            and p.deleted = false
            """)
    long countByAccountId(@Param("id") UUID id);

    @Query("""
            select count(p) from ProjectUserFavouriteEntity p
            where p.project.id in :ids
            and p.user <> p.project.authorAccount
            and p.project.isPrivate = false
            and p.deleted = false""")
//    @Cacheable(value = "projectUserStatsCache", key = "#userId + '_likesCount'", unless = "#result == null")
    long countByProjectIdIn(@Param("ids") Collection<UUID> ids);

    @Query("""
            select p from ProjectUserFavouriteEntity p
            where p.user = :user
            and p.project.isPrivate = false
            and p.project.deleted = false""")
    List<ProjectUserFavouriteEntity> findAllByUser(@Param("user") AccountEntity user);

    @Query("""
            select p from ProjectUserFavouriteEntity p
            where p.user = :user
            and p.project.isPrivate = false
            and p.project = :project""")
    Optional<ProjectUserFavouriteEntity> findByUserAndProject(
            @Param("user") AccountEntity user,
            @Param("project") ProjectInfoEntity project
    );

    @Query("""
            select (count(p) > 0) from ProjectUserFavouriteEntity p
            where p.project = :project
            and p.user = :user
            and p.deleted = false""")
    boolean existsByProjectAndUser(@Param("project") ProjectInfoEntity project, @Param("user") AccountEntity user);

}
