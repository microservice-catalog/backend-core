package ru.stepagin.dockins.core.project.service;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import ru.stepagin.dockins.api.v1.project.exception.EncryptionException;
import ru.stepagin.dockins.util.SecretStorageUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Converter
@RequiredArgsConstructor
public class CustomEnvDataEncryptor implements AttributeConverter<String, String> {

    private final SecretStorageUtil secretStorageUtil;

    private SecretKeySpec getKey() {
        byte[] keyBytes = secretStorageUtil.getDbEncryptionKey();
        return new SecretKeySpec(keyBytes, 0, 16, "AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            byte[] encrypted = cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new EncryptionException("Ошибка шифрования", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            byte[] decoded = Base64.getDecoder().decode(dbData);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionException("Ошибка расшифровки", e);
        }
    }
}

