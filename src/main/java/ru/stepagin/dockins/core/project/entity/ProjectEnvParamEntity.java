package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.stepagin.dockins.api.v1.project.service.CustomEnvDataEncryptor;

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

    private Boolean deleted;

    private LocalDateTime deletedOn;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @Transient
    public String getFormattedEnvLine() {
        if (defaultValue != null && !defaultValue.isEmpty()) {
            return name + "=" + defaultValue;
        } else {
            return name + "=";
        }
    }
}
