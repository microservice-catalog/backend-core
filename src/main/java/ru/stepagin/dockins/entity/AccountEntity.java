package ru.stepagin.dockins.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account", indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "email")
})
public class AccountEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    private String description;

    private String version;

    private Boolean deleted;

    private LocalDateTime deletedOn;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    private LocalDateTime lastLoginOn;

    private String avatarUrl;

    @Transient
    private List<ProjectInfoEntity> userProjects;
}
