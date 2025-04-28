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
    @JoinColumn(name = "project_id")
    private ProjectInfoEntity project;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagEntity tag;
}
