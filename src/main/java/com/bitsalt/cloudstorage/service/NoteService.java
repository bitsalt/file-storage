package com.bitsalt.cloudstorage.service;

import com.bitsalt.cloudstorage.mapper.NoteMapper;
import com.bitsalt.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public boolean addNote(Note note, Integer userId) {
        note.setUserId(userId);
        Integer id = noteMapper.insert(note);
        if (id != null) {
            return true;
        }
        return false;
    }

    public boolean saveEditedNote(Note note) {
        int result = noteMapper.update(note.getNoteTitle(), note.getNoteDescription(), note.getNoteId());
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteNote(Note note) {
        int result = noteMapper.delete(note.getNoteId());
        if (result > 0) {
            return true;
        }
        return false;
    }

    public List<Note> getUserNotes(int userId) {
        return this.noteMapper.getNotes(userId);
    }
}
