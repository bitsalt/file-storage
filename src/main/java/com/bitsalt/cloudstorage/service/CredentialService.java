package com.bitsalt.cloudstorage.service;

import com.bitsalt.cloudstorage.mapper.CredentialMapper;
import com.bitsalt.cloudstorage.model.CredentialForm;
import com.bitsalt.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public boolean addCredential(CredentialForm credentialForm, int userId) {
        Credential credential = new Credential(credentialForm.getUrl(),
                credentialForm.getUserName(), credentialForm.getPassword());
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
        credentialForm.setPassword(credential.getPassword());
        return credentialForm;
    }

    public boolean saveEditedCredential(CredentialForm credentialForm) {
        int result = this.credentialMapper.update(credentialForm.getUrl(), credentialForm.getUserName(),
                credentialForm.getPassword(), credentialForm.getCredentialId());
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
}
