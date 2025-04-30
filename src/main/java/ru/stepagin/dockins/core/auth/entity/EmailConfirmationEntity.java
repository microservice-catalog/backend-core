package ru.stepagin.dockins.core.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_email_confirmation_account_id")
    )
    private AccountEntity account;

    @Column(nullable = false)
    private String code; // 6-значный код подтверждения

    @Column(nullable = false)
    private LocalDateTime expirationTime; // до какого момента код активен

    @Builder.Default
    private Boolean used = false;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    private LocalDateTime updatedOn;

    public void markAsUsed() {
        this.setUsed(true);
        this.setUpdatedOn(LocalDateTime.now());
    }
}
