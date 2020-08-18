package com.bitsalt.cloudstorage.controller;

import com.bitsalt.cloudstorage.model.User;
import com.bitsalt.cloudstorage.service.CredentialService;
import com.bitsalt.cloudstorage.service.EncryptionService;
import com.bitsalt.cloudstorage.service.NoteService;
import com.bitsalt.cloudstorage.service.UserService;
import com.bitsalt.cloudstorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private EncryptionService encryptionService;


    @GetMapping
    public String getHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = this.userService.getUser(userName);
//        this.fileService.deleteAllFiles();
        model.addAttribute("userFiles", this.fileService.getFiles(user.getUserId()));
        model.addAttribute("userNotes", this.noteService.getUserNotes(user.getUserId()));
        model.addAttribute("userCredentials", this.credentialService.getUserCredentials(user.getUserId()));
        model.addAttribute("encryptionService", this.encryptionService);
        return "home";
    }
}
