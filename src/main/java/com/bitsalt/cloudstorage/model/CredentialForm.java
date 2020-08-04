package com.bitsalt.cloudstorage.model;

public class CredentialForm {
    private Integer credentialId;
    private String url;
    private String userName;
    private String password;
    private int userId;

    public Integer getCredentialId() {
        if (this.credentialId == null) {
            return 0;
        }
        return credentialId;
    }

    public void setCredentialId(Integer credentialId) {
        this.credentialId = credentialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
