package ru.stepagin.dockins.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.domain.user.entity.AccountEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByUsernameIgnoreCase(String username);

    Optional<AccountEntity> findByEmail(String email);
}