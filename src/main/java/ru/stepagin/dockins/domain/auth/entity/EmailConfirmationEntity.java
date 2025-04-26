package ru.stepagin.dockins.domain.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_confirmation", indexes = {
        @Index(columnList = "account_id")
})
public class EmailConfirmationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Column(nullable = false)
    private String code; // 6-значный код подтверждения

    @Column(nullable = false)
    private LocalDateTime expirationTime; // до какого момента код активен

    @Builder.Default
    private Boolean used = false;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    public void markAsUsed() {
        this.setUsed(true);
        this.setUpdatedOn(LocalDateTime.now());
    }
}
