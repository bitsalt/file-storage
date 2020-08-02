package com.bitsalt.cloudstorage.model;

public class NoteForm {
    private int noteId;
    private String noteTitle;
    private String noteDescription;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {

        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }
}
