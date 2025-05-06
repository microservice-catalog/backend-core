package ru.stepagin.dockins.core.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.core.storage.entity.ImageEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
    @Query("""
            select i from ImageEntity i
            where upper(i.account.username) = upper(:username)
            and i.deleted = false
            and i.account.deleted = false
            order by i.createdOn DESC""")
    @Transactional
    List<ImageEntity> findByUsername(@Param("username") String username);

    @Query("""
            select i from ImageEntity i
            where upper(i.account.username) = upper(:username)
            and i.deleted = false
            and i.account.deleted = false
            order by i.createdOn DESC
            limit 1""")
    @Transactional
    Optional<ImageEntity> findLastUserPhoto(@Param("username") String username);


}
