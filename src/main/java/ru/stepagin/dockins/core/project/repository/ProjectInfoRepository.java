package ru.stepagin.dockins.core.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfoEntity, UUID> {
    @Query("""
            select (count(p) > 0) from ProjectInfoEntity p
            where p.authorAccount = :authorAccount and upper(p.projectName) = upper(:projectName)""")
    boolean existsByAuthorAccountAndProjectName(
            @Param("authorAccount") AccountEntity authorAccount,
            @Param("projectName") String projectName
    );

    @Query("""
            select p from ProjectInfoEntity p
            where upper(p.authorAccount.username) = upper(:username)
            and upper(p.projectName) = upper(:projectName)""")
    Optional<ProjectInfoEntity> findByUsernameAndProjectName(
            @Param("username") String username,
            @Param("projectName") String projectName
    );

    @Query("""
            select p from ProjectInfoEntity p
            where p.authorAccount = :authorAccount
            and p.isPrivate = false
            and p.deleted = false""")
    Page<ProjectInfoEntity> findByAuthorAccountAndPrivateFalse(
            @Param("authorAccount") AccountEntity authorAccount,
            Pageable pageRequest
    );

    @Query("""
            select p from ProjectInfoEntity p
            where p.authorAccount.username = :username
            and p.isPrivate = false
            and p.deleted = false""")
    Page<ProjectInfoEntity> findByAuthorAccountAndPrivateFalse(
            @Param("username") String username,
            Pageable pageRequest
    );

    @Query("""
            select p from ProjectInfoEntity p
            where p.authorAccount = :authorAccount
            and p.isPrivate = true
            and p.deleted = false""")
    Page<ProjectInfoEntity> findByAuthorAccountAndPrivateTrue(
            @Param("authorAccount") AccountEntity authorAccount,
            Pageable pageRequest
    );


}
