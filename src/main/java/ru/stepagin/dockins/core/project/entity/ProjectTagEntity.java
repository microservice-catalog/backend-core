package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_tag")
public class ProjectTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_tag_project"))
    private ProjectInfoEntity project;

    @ManyToOne
    @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_project_tag_tag"))
    private TagEntity tag;
}
