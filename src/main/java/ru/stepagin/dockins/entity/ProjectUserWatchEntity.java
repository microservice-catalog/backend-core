package ru.stepagin.dockins.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private LocalDateTime createdOn;
}
