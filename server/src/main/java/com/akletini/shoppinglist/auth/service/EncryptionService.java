package com.akletini.shoppinglist.auth.service;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EncryptionService {

    public String encryptPassword(String password) {
        return Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
    }

    public boolean arePasswordsEqual(String original, String requested) {
        String encryptedRequest = encryptPassword(requested);
        return original.equals(encryptedRequest);
    }
}
