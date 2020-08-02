package com.bitsalt.cloudstorage.service;

import com.bitsalt.cloudstorage.mapper.NoteMapper;
import com.bitsalt.cloudstorage.model.Note;
import com.bitsalt.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public boolean addNote(NoteForm noteForm, int userId) {

        Note note = new Note(noteForm.getNoteTitle(), noteForm.getNoteDescription());
        note.setUserId(userId);
        int id = noteMapper.insert(note);
        if (id > 0) {
            return true;
        }
        return false;
    }

    public NoteForm getNoteForEditing(int noteId) {
        Note note = noteMapper.getSingleNote(noteId);
        NoteForm noteForm = new NoteForm();
        noteForm.setNoteDescription(note.getNoteDescription());
        noteForm.setNoteTitle(note.getNoteTitle());
        noteForm.setNoteId(note.getNoteId());
        return noteForm;
    }

    public boolean saveEditedNote(NoteForm noteForm) {
        int result = noteMapper.update(noteForm.getNoteTitle(), noteForm.getNoteDescription(), noteForm.getNoteId());
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteNote(NoteForm noteForm) {
        int result = noteMapper.delete(noteForm.getNoteId());
        if (result > 0) {
            return true;
        }
        return false;
    }

    public List<Note> getUserNotes(int userId) {
        return this.noteMapper.getNotes(userId);
    }
}
