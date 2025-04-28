package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tag", indexes = {
        @Index(columnList = "name", name = "idx_tag_name")
})
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();
}
