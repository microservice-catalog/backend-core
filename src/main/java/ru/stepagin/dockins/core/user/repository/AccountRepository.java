package ru.stepagin.dockins.core.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    @Query("select a from AccountEntity a where a.username = :username and a.deleted = false")
    Optional<AccountEntity> findByUsername(@Param("username") String username);

    @Query("select a from AccountEntity a where upper(a.username) = upper(:username) and a.deleted = false")
    Optional<AccountEntity> findByUsernameIgnoreCase(@Param("username") String username);

    Optional<AccountEntity> findByEmail(String email);

}