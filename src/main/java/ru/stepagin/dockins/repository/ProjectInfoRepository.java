package ru.stepagin.dockins.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.entity.ProjectInfoEntity;

import java.util.UUID;

@Repository
interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, UUID> {

}
