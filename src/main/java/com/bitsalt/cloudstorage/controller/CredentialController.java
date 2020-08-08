package com.bitsalt.cloudstorage.controller;

import com.bitsalt.cloudstorage.model.Credential;
import com.bitsalt.cloudstorage.model.User;
import com.bitsalt.cloudstorage.service.CredentialService;
import com.bitsalt.cloudstorage.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;
    private String errorMessage;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/credential/add")
    public String showCredResult(Authentication authentication, Credential credential, Model model) {
        String actionError = null;

        if (credential.getCredentialId() > 0) {
            return this.editCredential(credential, model);
        }

        User user = this.userService.getUser(authentication.getName());
        boolean result = this.credentialService.addCredential(credential, user.getUserId());

        if (result) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }

    private String editCredential(Credential credential, Model model) {
        boolean result = this.credentialService.saveEditedCredential(credential);
        if (result) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }

    @GetMapping("/credential/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") int credId, Credential credential, Model model) {
        String actionError = null;

        if (this.credentialService.deleteCredential(credential)) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }
}
