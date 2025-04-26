package ru.stepagin.dockins.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.domain.auth.entity.EmailConfirmationEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmationEntity, Long> {
    Optional<EmailConfirmationEntity> findTopByAccountIdOrderByCreatedOnDesc(UUID accountId);

    Optional<EmailConfirmationEntity> findTopByAccountEmailOrderByCreatedOnDesc(String email);
}
