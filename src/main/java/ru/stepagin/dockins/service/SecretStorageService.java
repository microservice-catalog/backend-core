package ru.stepagin.dockins.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.exception.EncryptionException;

import java.nio.charset.StandardCharsets;

@Service
public class SecretStorageService {

    private final byte[] masterSecret;

    public SecretStorageService(@Value("${app.secret.database-encryption-key}") String masterKey) throws Exception {
        if (masterKey == null) {
            throw new Exception("Master key not set");
        }
        byte[] keyBytes = masterKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 16) {
            throw new Exception("Master key must be exactly 16 bytes for AES-128 encryption, but there is: " + keyBytes.length + ".");
        }
        this.masterSecret = keyBytes;
    }

    public byte[] getEncryptionKey() {
        if (masterSecret.length != 16) {
            throw new EncryptionException("Master key is broken");
        }
        return masterSecret;
    }

}

