package com.bitsalt.cloudstorage.model;

public class Credential {

    private Integer credentialId;
    private String url;
    private String userName;
    private String password;
    private int userId;

    public Credential(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public Integer getCredentialId() {
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
