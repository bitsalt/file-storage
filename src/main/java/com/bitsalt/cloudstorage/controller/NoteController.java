package com.bitsalt.cloudstorage.controller;

import com.bitsalt.cloudstorage.mapper.UserMapper;
import com.bitsalt.cloudstorage.model.Note;
import com.bitsalt.cloudstorage.model.User;
import com.bitsalt.cloudstorage.service.NoteService;
import com.bitsalt.cloudstorage.service.UserService;
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
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/note/add")
    public String showNoteResult(Authentication authentication, Note note, Model model) {
        String actionError = null;

        if (note.getNoteId() > 0) {
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
        int noteId = note.getNoteId();
        result = this.noteService.saveEditedNote(note);

        if (result) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }

//    @PostMapping("/note/edit/{noteId}")
//    public String showNoteForEdit(Model model, int noteId) {
//        Note note = this.noteService.getNoteForEditing(noteId);
//        model.addAttribute("editNote", note);
//        return "home/#nav-notes";
//    }

    @GetMapping("/note/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") int noteId, Note note, Model model) {
        String actionError = null;

        if (this.noteService.deleteNote(note)) {
            model.addAttribute("actionSuccess", true);
        } else {
            model.addAttribute("actionFailure", true);
        }
        return "result";
    }

}
