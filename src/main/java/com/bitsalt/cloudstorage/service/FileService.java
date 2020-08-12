package com.bitsalt.cloudstorage.service;

import com.bitsalt.cloudstorage.mapper.FileMapper;
import com.bitsalt.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(Integer userId) {
        List<File> files = this.fileMapper.getFiles(userId);
        return files;
    }

    public File getFile(Integer fileId) {
        return this.fileMapper.getFile(fileId);
    }


    public boolean saveFile(MultipartFile multipartFile, Integer userId) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        File file = new File(null,
                multipartFile.getOriginalFilename(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                multipartFile.getBytes(),
                userId
        );
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

    public boolean isFileNameUsed(String fileName, int userId) {
        int fileCount = this.fileMapper.findExistingFile(fileName, userId);
        if (fileCount > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteFile(int fileId) {
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

