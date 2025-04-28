package ru.stepagin.dockins.core.project.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_user_watch")
public class ProjectUserWatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AccountEntity user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectInfoEntity project;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();
}
