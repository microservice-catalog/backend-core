package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.TagEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);

    List<TagEntity> findAllByNameIn(Collection<String> names);


}
