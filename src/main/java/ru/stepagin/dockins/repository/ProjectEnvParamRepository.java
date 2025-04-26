package ru.stepagin.dockins.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.entity.ProjectEnvParamEntity;

@Repository
public interface ProjectEnvParamRepository extends JpaRepository<ProjectEnvParamEntity, Long> {

}
