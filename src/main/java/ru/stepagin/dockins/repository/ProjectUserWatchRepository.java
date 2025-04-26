package ru.stepagin.dockins.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.entity.ProjectUserWatchEntity;

@Repository
interface ProjectUserWatchRepository extends JpaRepository<ProjectUserWatchEntity, Long> {

}

