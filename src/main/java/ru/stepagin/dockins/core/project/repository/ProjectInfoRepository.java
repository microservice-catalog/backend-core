package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;

import java.util.UUID;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, UUID> {

}
