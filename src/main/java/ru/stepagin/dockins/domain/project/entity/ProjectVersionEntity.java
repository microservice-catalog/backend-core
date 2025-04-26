package ru.stepagin.dockins.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectInfoEntity project;

    @Column(nullable = false)
    private String name;

    private String version;

    private Boolean deleted;

    private LocalDateTime deletedOn;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private String linkGithub;

    private String linkDockerhub;

    private String dockerCommand;

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

}
