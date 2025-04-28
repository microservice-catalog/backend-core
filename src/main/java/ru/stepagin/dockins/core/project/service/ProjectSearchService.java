package ru.stepagin.dockins.core.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.project.entity.ProjectTagEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectSearchService {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<ProjectInfoEntity> searchProjects(String query, List<String> tags, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // === Основной запрос на получение данных ===
        CriteriaQuery<ProjectInfoEntity> cq = cb.createQuery(ProjectInfoEntity.class);
        Root<ProjectInfoEntity> project = cq.from(ProjectInfoEntity.class);
        Join<ProjectInfoEntity, ProjectTagEntity> projectTags = project.join("tags", JoinType.INNER);

        List<Predicate> predicates = buildPredicates(cb, project, projectTags, query, tags);

        cq.select(project)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        if (tags != null && !tags.isEmpty()) {
            cq.groupBy(project.get("id"));
            cq.having(cb.equal(cb.countDistinct(projectTags.get("tag").get("name")), (long) tags.size()));
        }

        List<ProjectInfoEntity> projects = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // === Отдельный запрос на подсчёт общего количества ===
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ProjectInfoEntity> countRoot = countQuery.from(ProjectInfoEntity.class);
        Join<ProjectInfoEntity, ProjectTagEntity> countTags = countRoot.join("tags", JoinType.INNER);

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, countTags, query, tags);

        countQuery.select(cb.countDistinct(countRoot.get("id")))
                .where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(projects, pageable, total);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb,
                                            Root<ProjectInfoEntity> project,
                                            Join<ProjectInfoEntity, ProjectTagEntity> projectTags,
                                            String query,
                                            List<String> tags) {
        List<Predicate> predicates = new ArrayList<>();

        if (query != null && !query.isBlank()) {
            String[] keywords = query.trim().split("\\s+");

            for (String keyword : keywords) {
                String loweredKeyword = "%" + keyword.toLowerCase() + "%";

                Predicate titlePredicate = cb.like(cb.lower(project.get("title")), loweredKeyword);
                Predicate descriptionPredicate = cb.like(cb.lower(project.get("description")), loweredKeyword);

                predicates.add(cb.or(titlePredicate, descriptionPredicate));
            }
        }

        if (tags != null && !tags.isEmpty()) {
            predicates.add(projectTags.get("tag").get("name").in(tags));
        }

        return predicates;
    }
}
