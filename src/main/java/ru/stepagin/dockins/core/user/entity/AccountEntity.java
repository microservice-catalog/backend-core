package ru.stepagin.dockins.core.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.stepagin.dockins.core.common.exception.BadUpdateDataException;
import ru.stepagin.dockins.core.project.entity.ProjectInfoEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account", indexes = {
        @Index(columnList = "username", name = "idx_account_username"),
        @Index(columnList = "email", name = "idx_account_email")
})
public class AccountEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Builder.Default
    @Column(nullable = false)
    private Boolean emailConfirmed = false; // подтверждён ли email

    @Column(nullable = false)
    private String password;

    private String fullName;

    private String description;

    @Version
    private Long version;

    @Builder.Default
    private boolean deleted = false;

    private LocalDateTime deletedOn;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    private LocalDateTime updatedOn;

    private LocalDateTime lastLoginOn;

    private String avatarUrl;

    @Transient
    private List<ProjectInfoEntity> userProjects;

    public void goodFieldsOrThrow() {
        if (fullName != null && fullName.trim().length() > 30)
            throw new BadUpdateDataException("Длина имени должна быть не более 30 символов.");
        if (description != null && description.trim().length() > 100)
            throw new BadUpdateDataException("Длина описания должна быть не более 100 символов.");
    }
}
