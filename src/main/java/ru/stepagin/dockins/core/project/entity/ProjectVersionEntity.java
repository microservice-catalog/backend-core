package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import ru.stepagin.dockins.core.common.exception.BadUpdateDataException;

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
@Table(name = "project_version", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "project_id"})
})
public class ProjectVersionEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
    private ProjectInfoEntity project;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    private boolean isPrivate = false;

    private String linkGithub;

    private String linkDockerhub;

    private String dockerCommand;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    private LocalDateTime updatedOn;

    @Builder.Default
    private boolean deleted = false;

    private LocalDateTime deletedOn;

    @Version
    private Long version;

    public void markAsDeleted() {
        this.deleted = true;
        this.deletedOn = LocalDateTime.now();
    }

    @Transient
    private List<ProjectEnvParamEntity> envParams;

    @Transient
    public String getEnvFileContent() {
        if (envParams == null || envParams.isEmpty()) {
            return "";
        }

        return envParams.stream()
                .sorted((a, b) -> {
                    // Сначала обязательные, потом необязательные
                    int requiredCompare = Boolean.compare(!a.getRequired(), !b.getRequired());
                    if (requiredCompare != 0) {
                        return requiredCompare;
                    }
                    // Внутри групп сортируем по алфавиту по имени
                    return a.getName().compareToIgnoreCase(b.getName());
                })
                .map(ProjectEnvParamEntity::getFormattedEnvLine)
                .collect(Collectors.joining("\n"));
    }

    public void goodFieldsOrThrow() {
        if (!this.name.matches("^[a-zA-Z0-9-.]$"))
            throw new BadUpdateDataException("Название версии может содержать латинские буквы, цифры, дефис и знак точки.");
    }
}
