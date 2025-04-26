package ru.stepagin.dockins.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.stepagin.dockins.domain.auth.entity.AccountEntity;

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

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private Boolean isPrivate;

    @Transient
    private List<TagEntity> tags;

    @Transient
    private Long favouritesCount;

    @Transient
    private Long pullsCount;

    @Transient
    private Long watchesCount;
}
