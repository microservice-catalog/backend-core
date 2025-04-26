package ru.stepagin.dockins.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.exception.EncryptionException;

import java.nio.charset.StandardCharsets;

@Service
public class SecretStorageService {

    private final byte[] dbSecret;
    private final byte[] jwtSecret;

    public SecretStorageService(
            @Value("${app.secret.database-encryption-key}") String databaseEncryptKey,
            @Value("${app.secret.jwt.signature-key}") String jwtSignatureKey
    ) throws Exception {
        if (databaseEncryptKey == null) {
            throw new Exception("Master db key not set");
        }
        byte[] dbSecretBytes = databaseEncryptKey.getBytes(StandardCharsets.UTF_8);
        if (dbSecretBytes.length != 16) {
            throw new Exception("Master key must be exactly 16 bytes for AES-128 encryption, but there is: " + dbSecretBytes.length + ".");
        }
        this.dbSecret = dbSecretBytes;

        if (jwtSignatureKey == null) {
            throw new Exception("Master jwt signature key not set");
        }
        this.jwtSecret = jwtSignatureKey.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getDbEncryptionKey() {
        if (dbSecret.length != 16) {
            throw new EncryptionException("Master db key is broken");
        }
        return dbSecret;
    }

    public byte[] getJwtSignatureKey() {
        return jwtSecret;
    }
}

