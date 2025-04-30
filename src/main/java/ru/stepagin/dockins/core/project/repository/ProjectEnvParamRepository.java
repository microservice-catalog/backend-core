package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectEnvParamEntity;
import ru.stepagin.dockins.core.project.entity.ProjectVersionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectEnvParamRepository extends JpaRepository<ProjectEnvParamEntity, Long> {

    @Query("""
            select p from ProjectEnvParamEntity p
            where p.projectVersion = :projectVersion
            and upper(p.name) = upper(:name)""")
    Optional<ProjectEnvParamEntity> findByProjectVersionAndNameWithDeleted(@Param("projectVersion") ProjectVersionEntity projectVersion, @Param("name") String name);


    @Query("""
            select p from ProjectEnvParamEntity p
            where p.projectVersion = :projectVersion
            and p.deleted = false""")
    List<ProjectEnvParamEntity> findByProjectVersion(@Param("projectVersion") ProjectVersionEntity projectVersion);


}
