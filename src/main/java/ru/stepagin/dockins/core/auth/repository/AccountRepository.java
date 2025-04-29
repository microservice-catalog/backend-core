package ru.stepagin.dockins.core.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    @Query("select a from AccountEntity a where a.username = :username and a.deleted = false")
    Optional<AccountEntity> findByUsernameExactly(@Param("username") String username);

    @Query("""
            select a from AccountEntity a
            where upper(a.username) = upper(:username)
            and a.deleted = false""")
    Optional<AccountEntity> findByUsernameIgnoreCase(@Param("username") String username);

    Optional<AccountEntity> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update AccountEntity a set a.lastLoginOn = CURRENT_TIMESTAMP where a.id = :id")
    void updateLastLogin(@Param("id") UUID id);

}