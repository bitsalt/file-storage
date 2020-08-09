package com.bitsalt.cloudstorage.controller;

import com.bitsalt.cloudstorage.model.User;
import com.bitsalt.cloudstorage.service.CredentialService;
import com.bitsalt.cloudstorage.service.EncryptionService;
import com.bitsalt.cloudstorage.service.NoteService;
import com.bitsalt.cloudstorage.service.UserService;
import com.bitsalt.cloudstorage.service.FileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final NoteService noteService;
    private final UserService userService;
    private final FileService fileService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(NoteService noteService, UserService userService, FileService fileService, CredentialService credentialService, EncryptionService encryptionService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        //this.userId = user.getUserId();
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = this.userService.getUser(userName);
        model.addAttribute("userFiles", this.fileService.getFiles(user.getUserId()));
        model.addAttribute("userNotes", this.noteService.getUserNotes(user.getUserId()));
        model.addAttribute("userCredentials", this.credentialService.getUserCredentials(user.getUserId()));
        model.addAttribute("encryptionService", this.encryptionService);
        return "home";
    }
}
