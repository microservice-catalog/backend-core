package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    @Column(nullable = false, unique = true)
    private String projectName;

    @ManyToOne
    @JoinColumn(name = "author_account_id")
    private AccountEntity authorAccount;

    @ManyToOne
    @JoinColumn(name = "default_project_version")
    private ProjectVersionEntity defaultProjectVersion;

    private String title;

    private String description;

    private String version;

    private Boolean deleted;

    private LocalDateTime deletedOn;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    private LocalDateTime updatedOn;

    @Builder.Default
    private Boolean isPrivate = false;

    @Transient
    private List<TagEntity> tags;

    public void markAsDeleted() {
        setDeleted(true);
        setDeletedOn(java.time.LocalDateTime.now());
    }

    @Transient
    public List<String> getTagsAsString() {
        return this.tags.stream()
                .map(TagEntity::getName)
                .toList();
    }
}
