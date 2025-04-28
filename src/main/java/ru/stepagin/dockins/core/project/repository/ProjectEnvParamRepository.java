package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectEnvParamEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;

import java.util.Optional;

@Repository
public interface ProjectEnvParamRepository extends JpaRepository<ProjectEnvParamEntity, Long> {
    @Query("""
            select (count(p) > 0) from ProjectEnvParamEntity p
            where p.projectVersion = :projectVersion
            and upper(p.name) = upper(:name)""")
    boolean existsByProjectVersionAndName(@Param("projectVersion") ProjectVersionEntity projectVersion, @Param("name") String name);

    @Query("""
            select p from ProjectEnvParamEntity p
            where p.projectVersion = :projectVersion
            and upper(p.name) = upper(:name)
            and p.deleted = false""")
    Optional<ProjectEnvParamEntity> findByProjectVersionAndName(@Param("projectVersion") ProjectVersionEntity projectVersion, @Param("name") String name);


}
