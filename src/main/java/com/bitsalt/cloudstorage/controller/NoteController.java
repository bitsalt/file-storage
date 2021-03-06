package com.bitsalt.cloudstorage.controller;

import com.bitsalt.cloudstorage.mapper.UserMapper;
import com.bitsalt.cloudstorage.model.Note;
import com.bitsalt.cloudstorage.model.User;
import com.bitsalt.cloudstorage.service.NoteService;
import com.bitsalt.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;


    @PostMapping("/note/add")
    public String showNoteResult(Authentication authentication, Note note, Model model) {

        if (note.getNoteId() != null) {
            return this.editNote(note, model);
        }
        boolean result;
        User user = this.userService.getUser(authentication.getName());
        result = this.noteService.addNote(note, user.getUserId());

        if (result) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }


    private String editNote(Note note, Model model) {
        String actionError = null;

        boolean result;
        Integer noteId = note.getNoteId();
        result = this.noteService.saveEditedNote(note);

        if (result) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }


    @GetMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Note note, Model model) {
        String actionError = null;

        if (this.noteService.deleteNote(note)) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }

}
