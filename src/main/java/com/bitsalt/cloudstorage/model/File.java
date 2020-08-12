package com.bitsalt.cloudstorage.model;

import java.sql.Blob;


public class File {

    private Integer fileId;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private Integer userId; // foreign key users.userid
    private byte[] fileData;

    public File(Integer fileId, String fileName) {
        this.fileId = fileId;
        this.fileName = fileName;
    }

    public File(Integer fileId, String fileName, String contentType, Long fileSize, byte[] fileData, Integer userId) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.fileData = (byte[])fileData;
        this.userId = userId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
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

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getFiledata() {
        return (byte[] )this.fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}