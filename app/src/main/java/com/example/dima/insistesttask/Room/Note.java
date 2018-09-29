package com.example.dima.insistesttask.Room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Note {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int noteId;
    @ColumnInfo(name = "noteName")
    private String noteName;
    @ColumnInfo(name = "noteDescription")
    private String noteDescription;
    @ColumnInfo(name = "color")
    private int color;
    public Note(String noteName, String noteDescription){
        this.noteName = noteName;
        this.noteDescription = noteDescription;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @NonNull
    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(@NonNull int noteId) {
        this.noteId = noteId;
    }
}
