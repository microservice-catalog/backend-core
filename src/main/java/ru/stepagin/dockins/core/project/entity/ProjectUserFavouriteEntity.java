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
@Table(name = "project_user_favourite")
public class ProjectUserFavouriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_project_user_favourite_user"))
    private AccountEntity user;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_user_favourite_project"))
    private ProjectInfoEntity project;

    @Builder.Default
    private LocalDateTime createdOn = LocalDateTime.now();

    @Builder.Default
    private boolean deleted = false;

    private LocalDateTime deletedOn;

    public void markAsDeleted() {
        setDeleted(true);
        setDeletedOn(LocalDateTime.now());
    }
}