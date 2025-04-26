package ru.stepagin.dockins.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stepagin.dockins.entity.AccountEntity;

import java.util.UUID;

@Repository
interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

}