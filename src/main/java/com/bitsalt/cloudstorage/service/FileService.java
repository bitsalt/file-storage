package com.bitsalt.cloudstorage.service;

import com.bitsalt.cloudstorage.mapper.FileMapper;
import com.bitsalt.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;


    public List<File> getFiles(Integer userId) {
        List<File> files = this.fileMapper.getFiles(userId);
        return files;
    }

    public File getFile(Integer fileId) {
        return this.fileMapper.getFile(fileId);
    }

    public boolean saveFile(MultipartFile multipartFile, Integer userId) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        File file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setFileSize(multipartFile.getSize());
        file.setContentType(multipartFile.getContentType());
        file.setFileData(multipartFile.getBytes());
        file.setUserId(userId);

        Integer id;
        try {
            if (file.getFileId() == null) {
                id = this.fileMapper.insert(file);
            } else {
                id = this.fileMapper.update(file.getFileName(), file.getContentType(),
                        file.getFileSize(), file.getFiledata(), file.getFileId());
            }
        } catch (Exception e) {
            return false;
        }

        if (id != null) {
            return true;
        }
        return false;
    }

    public boolean isFileNameUsed(String fileName, Integer userId) {
        int fileCount = this.fileMapper.findExistingFile(fileName, userId);
        if (fileCount > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteFile(Integer fileId) {
        int id = this.fileMapper.delete(fileId);
        if (id > 0) {
            return true;
        }
        return false;
    }

    public void deleteAllFiles() {
        this.fileMapper.deleteAll();
    }
}

