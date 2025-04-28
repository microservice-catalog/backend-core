package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.TagEntity;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

}
