package ru.stepagin.dockins.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.domain.project.entity.ProjectUserFavouriteEntity;

@Repository
public interface ProjectUserFavouriteRepository extends JpaRepository<ProjectUserFavouriteEntity, Long> {

}
