package ru.stepagin.dockins.core.project.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
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

    public List<ProjectInfoEntity> searchProjects(String query, List<String> tags, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProjectInfoEntity> cq = cb.createQuery(ProjectInfoEntity.class);
        Root<ProjectInfoEntity> project = cq.from(ProjectInfoEntity.class);
        Join<ProjectInfoEntity, ProjectTagEntity> projectTags = project.join("tags", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        // 1. Разбираем строку query по словам
        if (query != null && !query.isBlank()) {
            String[] keywords = query.trim().split("\\s+");

            for (String keyword : keywords) {
                String loweredKeyword = "%" + keyword.toLowerCase() + "%";

                Predicate titlePredicate = cb.like(cb.lower(project.get("title")), loweredKeyword);
                Predicate descriptionPredicate = cb.like(cb.lower(project.get("description")), loweredKeyword);

                // Каждое ключевое слово должно хотя бы где-то встретиться
                predicates.add(cb.or(titlePredicate, descriptionPredicate));
            }
        }

        // 2. Фильтрация по тегам
        if (tags != null && !tags.isEmpty()) {
            predicates.add(projectTags.get("tag").get("name").in(tags));
            // HAVING для точного совпадения всех тегов
            cq.groupBy(project.get("id"));
            cq.having(cb.equal(cb.countDistinct(projectTags.get("tag").get("name")), (long) tags.size()));
        }

        cq.select(project)
                .where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    public List<ProjectInfoEntity> search(String query, List<String> tags, PageRequest pageRequest) {
        return null;
    }
}
