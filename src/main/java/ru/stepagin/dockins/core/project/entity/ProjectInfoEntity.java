package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.stepagin.dockins.core.DomainErrorCodes;
import ru.stepagin.dockins.core.project.exception.ProjectConstraintViolationException;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_info",
        indexes = {
                @Index(columnList = "projectName", name = "idx_project_info_project_name"),
                @Index(columnList = "authorAccountId", name = "idx_project_info_author_account_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"author_account_id", "project_name"})
        }
)
public class ProjectInfoEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false)
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "author_account_id")
    private AccountEntity authorAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "default_project_version", nullable = false)
    private ProjectVersionEntity defaultProjectVersion;

    private String title;

    private String description;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    private LocalDateTime updatedOn;

    @Builder.Default
    private boolean isPrivate = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_tag", // имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "project_id"),  // колонка, которая ссылается на ProjectInfoEntity
            inverseJoinColumns = @JoinColumn(name = "tag_id") // колонка, которая ссылается на TagEntity
    )
    private List<TagEntity> tags;

    @Version
    private Long version;

    @Builder.Default
    private boolean deleted = false;

    private LocalDateTime deletedOn;

    public void markAsDeleted() {
        setDeleted(true);
        setDeletedOn(java.time.LocalDateTime.now());
    }

    // Метод для получения строковых представлений тегов
    @Transient
    public List<String> getTagsAsString() {
        if (tags == null) {
            return List.of();
        }
        return this.tags.stream()
                .map(TagEntity::getName)
                .collect(Collectors.toList());
    }

    public void goodFieldsOrThrow() {
        if (projectName.length() < 5)
            throw new ProjectConstraintViolationException("Название проекта должно быть от 5 до 50 символов.", DomainErrorCodes.PROJECT_NAME_IS_TOO_SHORT);
        if (projectName.length() > 50)
            throw new ProjectConstraintViolationException("Название проекта должно быть от 5 до 50 символов.", DomainErrorCodes.PROJECT_NAME_IS_TOO_LONG);

        if (!projectName.matches("^[a-zA-Z].*$"))
            throw new ProjectConstraintViolationException("Название проекта должно начинаться с латинской буквы.", DomainErrorCodes.PROJECT_NAME_STARTS_WITH_BAD_SYMBOL);
        if (!projectName.matches("^.*[a-zA-Z0-9]$"))
            throw new ProjectConstraintViolationException("Название проекта должно заканчиваться на латинскую букву или цифру.", DomainErrorCodes.PROJECT_NAME_ENDS_WITH_BAD_SYMBOL);
        if (!projectName.matches("^[a-zA-Z][a-zA-Z0-9-]*[a-zA-Z0-9]$"))
            throw new ProjectConstraintViolationException("Название проекта может содержать только латинские буквы, цифры и знак дефис.", DomainErrorCodes.PROJECT_NAME_CONTAINS_BAD_SYMBOL);

    }
}
