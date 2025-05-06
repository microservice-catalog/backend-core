package ru.stepagin.dockins.core.storage.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stepagin.dockins.core.storage.exception.BadContentTypeException;
import ru.stepagin.dockins.core.storage.exception.BadFileException;
import ru.stepagin.dockins.core.storage.exception.LargeFileException;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_image_entity_account_id")
    )
    private AccountEntity account;

    @Lob
    @Column(nullable = false)
    private byte[] bytes;

    private String name;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String contentType;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    private LocalDateTime uploadedOn;

    @Builder.Default
    private boolean deleted = false;

    private LocalDateTime deletedOn;

    public static ImageEntity of(MultipartFile img, AccountEntity owner) {
        if (!Objects.equals(img.getContentType(), "image/jpeg") &&
                !Objects.equals(img.getContentType(), "image/png")) {
            throw new BadContentTypeException("Bad file format for image '" + img.getOriginalFilename() + "'");
        }

        if (img.getSize() > 10 * 1024 * 1024) {
            throw new LargeFileException("Too large image size for image '" + img.getOriginalFilename() + "'");
        }

        try {
            return ImageEntity.builder()
                    .contentType(img.getContentType())
                    .size(img.getSize())
                    .bytes(img.getBytes())
                    .name(img.getOriginalFilename())
                    .account(owner)
                    .build();
        } catch (IOException e) {
            throw new BadFileException("Error while reading image '" + img.getOriginalFilename() + "'");
        }
    }

    public void markAsDeleted() {
        this.deleted = true;
        this.deletedOn = LocalDateTime.now();
    }
}