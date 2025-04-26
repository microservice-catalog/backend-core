package ru.stepagin.dockins.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.domain.project.entity.ProjectVersionEntity;

import java.util.UUID;

@Repository
public interface ProjectVersionRepository extends JpaRepository<ProjectVersionEntity, UUID> {

}