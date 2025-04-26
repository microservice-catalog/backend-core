package ru.stepagin.dockins.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.stepagin.dockins.domain.user.entity.AccountEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_user_favourite")
public class ProjectUserFavouriteEntity {
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

    private Boolean deleted;

    private LocalDateTime deletedOn;
}