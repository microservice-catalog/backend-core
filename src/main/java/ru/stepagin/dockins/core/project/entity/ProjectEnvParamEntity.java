package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.stepagin.dockins.core.common.exception.BadUpdateDataException;

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
    @JoinColumn(name = "project_version_id",
            foreignKey = @ForeignKey(name = "fk_project_env_param_project_version")
    )
    private ProjectVersionEntity projectVersion;

    //    @Convert(converter = CustomEnvDataEncryptor.class)
    @Column(nullable = false)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private boolean required = false;

    //    @Convert(converter = CustomEnvDataEncryptor.class)
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

    public void goodFieldsOrThrow() {
        if (this.name == null || this.name.isBlank())
            throw new BadUpdateDataException("Название параметра не может быть пустым");

        if (!this.name.matches("^[a-zA-Z0-9._-]+$"))
            throw new BadUpdateDataException("Название параметра может содержать латинские буквы, цифры, дефис, нижнее подчёркивание и знак точки.");
    }

    public void markAsDeleted() {
        this.setDeleted(true);
        this.setDeletedOn(LocalDateTime.now());
    }

    public void markAsRestored() {
        this.deleted = false;
    }
}
