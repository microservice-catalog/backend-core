package ru.stepagin.dockins.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.entity.TagEntity;

@Repository
interface TagRepository extends JpaRepository<TagEntity, Long> {

}
