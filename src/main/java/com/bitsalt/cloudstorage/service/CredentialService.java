package com.bitsalt.cloudstorage.service;

import com.bitsalt.cloudstorage.mapper.CredentialMapper;
import com.bitsalt.cloudstorage.model.CredentialForm;
import com.bitsalt.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;
import java.util.Hashtable;

import java.security.SecureRandom;
import java.util.*;
import java.lang.*;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;
    private Object returnObject;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public boolean addCredential(CredentialForm credentialForm, int userId) {
        Hashtable<String, String> hash = this.encryptPassword(credentialForm.getPassword());
        Credential credential = new Credential(credentialForm.getUrl(),
                credentialForm.getUserName(), hash.get("key"), hash.get("password"));
        credential.setUserId(userId);
        int credId = this.credentialMapper.insert(credential);
        if (credId > 0) {
            return true;
        }
        return false;
    }

    public CredentialForm getCredForEditing(int credId) {
        Credential credential = this.credentialMapper.getSingleCredential(credId);
        CredentialForm credentialForm = new CredentialForm();
        credentialForm.setCredentialId(credential.getCredentialId());
        credentialForm.setUserId(credential.getUserId());
        credentialForm.setUrl(credential.getUrl());
        credentialForm.setUserName(credential.getUserName());
        credentialForm.setPassword(this.encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        return credentialForm;
    }

    public boolean saveEditedCredential(CredentialForm credentialForm) {
        Hashtable<String, String> hash = this.encryptPassword(credentialForm.getPassword());
        int result = this.credentialMapper.update(credentialForm.getUrl(), credentialForm.getUserName(),
               hash.get("key"), hash.get("password"), credentialForm.getCredentialId());
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteCredential(CredentialForm credentialForm) {
        int result = this.credentialMapper.delete(credentialForm.getCredentialId());
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
