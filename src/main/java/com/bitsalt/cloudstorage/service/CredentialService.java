package com.bitsalt.cloudstorage.service;

import com.bitsalt.cloudstorage.mapper.CredentialMapper;
import com.bitsalt.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Hashtable;

import java.security.SecureRandom;
import java.util.*;
import java.lang.*;

@Service
@Transactional
public class CredentialService {
    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private EncryptionService encryptionService;


    public boolean addCredential(Credential credential, Integer userId) {
        Hashtable<String, String> hash = this.encryptPassword(credential.getPassword());
        credential.setUserId(userId);
        credential.setKey(hash.get("key"));
        credential.setPassword(hash.get("password"));
        Integer credId = this.credentialMapper.insert(credential);
        if (credId != null) {
            return true;
        }
        return false;
    }

    public boolean saveEditedCredential(Credential credential) {
        int result = 0;
        try {
            Hashtable<String, String> hash = this.encryptPassword(credential.getPassword());
            result = this.credentialMapper.update(credential.getUrl(), credential.getUserName(),
                    hash.get("key"), hash.get("password"), credential.getCredentialId());
        } catch (Exception e) {
            System.out.println("Exception in saveEditedCredential() Error: " + e);
        }

        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteCredential(Credential credential) {
        int result = this.credentialMapper.delete(credential.getCredentialId());
        if (result > 0) {
            return true;
        }
        return false;
    }

    public List<Credential> getUserCredentials(int userId) {
        return this.credentialMapper.getCredentials(userId);
    }

    private Hashtable<String, String> encryptPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = this.encryptionService.encryptValue(password, encodedKey);

        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("key", encodedKey);
        hashtable.put("password", encryptedPassword);

        return hashtable;
    }

}
