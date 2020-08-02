package com.bitsalt.cloudstorage.model;

public class Note {
    private int noteId;
    private String noteTitle;
    private String noteDescription;
    private int userId; // foreign key users.userid

    public Note(String noteTitle, String noteDescription) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) { this.noteId = noteId; }
    public void setnoteid(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
