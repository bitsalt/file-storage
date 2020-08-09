package com.bitsalt.cloudstorage.controller;

import com.bitsalt.cloudstorage.model.File;
import com.bitsalt.cloudstorage.model.User;
import com.bitsalt.cloudstorage.service.FileService;
import com.bitsalt.cloudstorage.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("file/save")
    public String saveFile(Authentication authentication, MultipartFile fileUpload, Model model) throws IOException {
        User user = this.userService.getUser(authentication.getName());

        if (this.fileService.isFileNameUsed(fileUpload.getName(), user.getUserId())) {
            model.addAttribute("actionError", "File names must be unique.");
            return "result";
        }

        boolean result = false;
        try {
            result = this.fileService.saveFile(fileUpload, user.getUserId());
            model.addAttribute("fileUploadSuccess", "File successfully uploaded.");
        } catch (Exception e) {
            String uploadError = e.toString();
            model.addAttribute("actionError", uploadError);
        }

        if (result) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFail", true);
        }
        return "result";
    }

    @PostMapping("/file/delete/{id}")
    public String deleteFile(@PathVariable("fileId") int fileId, Model model) {
        if (this.fileService.deleteFile(fileId)) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }

    @GetMapping("/file/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable("fileId") int fileId, Model model) {
        File file = this.fileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getFiledata());
    }

}