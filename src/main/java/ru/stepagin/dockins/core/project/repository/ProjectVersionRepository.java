package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectVersionRepository extends JpaRepository<ProjectVersionEntity, UUID> {
    @Query("""
            select (count(p) > 0) from ProjectVersionEntity p
            where p.project = :project
            and upper(p.name) = upper(:name)""")
    boolean existsByProjectAndName(
            @Param("project") ProjectInfoEntity project,
            @Param("name") String name
    );

    @Query("""
            select p from ProjectVersionEntity p
            where upper(p.project.authorAccount.username) = upper(:username)
            and upper(p.project.projectName) = upper(:projectName)
            and upper(p.name) = upper(:name)""")
    Optional<ProjectVersionEntity> findByProjectNameAndVersionName(
            @Param("username") String username,
            @Param("projectName") String projectName,
            @Param("name") String name
    );


}