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

    // только для регистрации
    @Query("select a from AccountEntity a where a.username = :username and a.deleted = false")
    Optional<AccountEntity> findByUsernameExactlyForRegistration(@Param("username") String username);

    // только для регистрации
    Optional<AccountEntity> findByEmailForRegistration(String email);

    @Query("""
            select a from AccountEntity a
            where upper(a.username) = upper(:username)
            and a.deleted = false
            and a.emailConfirmed = true""")
    Optional<AccountEntity> findByUsernameIgnoreCase(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("update AccountEntity a set a.lastLoginOn = CURRENT_TIMESTAMP where a.id = :id")
    void updateLastLogin(@Param("id") UUID id);

    @Query("""
            select (count(a) > 0) from AccountEntity a
            where upper(a.username) = upper(:username)
            and a.deleted = false
            and a.emailConfirmed = true""")
    boolean existsByUsername(@Param("username") String username);

}