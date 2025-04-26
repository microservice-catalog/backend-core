package ru.stepagin.dockins.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.entity.ProjectVersionEntity;

import java.util.UUID;

@Repository
interface ProjectVersionRepository extends JpaRepository<ProjectVersionEntity, UUID> {

}