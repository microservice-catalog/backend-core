package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.stepagin.dockins.core.project.service.CustomEnvDataEncryptor;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_env_param",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"project_version_id", "name"})
        },
        indexes = {
                @Index(columnList = "projectVersionId", name = "idx_project_env_param_project_version_id")
        }
)
public class ProjectEnvParamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_version_id")
    private ProjectVersionEntity projectVersion;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean required;

    @Convert(converter = CustomEnvDataEncryptor.class)
    private String defaultValue;

    @Builder.Default
    private boolean deleted = false;

    private LocalDateTime deletedOn;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    private LocalDateTime updatedOn;

    @Transient
    public String getFormattedEnvLine() {
        if (defaultValue != null && !defaultValue.isEmpty()) {
            return name + "=" + defaultValue;
        } else {
            return name + "=";
        }
    }

    public void markAsDeleted() {
        this.setDeleted(true);
        this.setDeletedOn(LocalDateTime.now());
    }
}
