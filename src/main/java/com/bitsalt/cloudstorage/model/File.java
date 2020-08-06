package com.bitsalt.cloudstorage.model;

public class File {

    private Integer fileId;
    private String fileName;
    private String contentType;
    private String fileSize;
    private int userId; // foreign key users.userid
    private byte filedata;

    public File(int fileId, String fileName, String contentType, String fileSize, int userId, byte filedata) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.userId = userId;
        this.filedata = filedata;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte getFiledata() {
        return filedata;
    }

    public void setFiledata(byte filedata) {
        this.filedata = filedata;
    }
}
