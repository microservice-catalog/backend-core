package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.TagEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByName(String name);

    List<TagEntity> findAllByNameIn(Collection<String> names);

    @Query("select t from TagEntity t where lower(t.name) like lower(concat(:name, '%'))")
    List<TagEntity> findAllLike(@Param("name") String name, Pageable pageRequest);

    @Query("select t.tag from ProjectTagEntity t group by t.tag order by count(t.project) desc")
    List<TagEntity> findRelevant(Pageable pageRequest);

}
