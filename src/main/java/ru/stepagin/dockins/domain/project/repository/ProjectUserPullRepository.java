package ru.stepagin.dockins.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.domain.project.entity.ProjectUserPullEntity;

@Repository
public interface ProjectUserPullRepository extends JpaRepository<ProjectUserPullEntity, Long> {

}