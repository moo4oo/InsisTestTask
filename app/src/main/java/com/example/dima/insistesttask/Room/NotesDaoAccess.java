package com.example.dima.insistesttask.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotesDaoAccess {
    @Insert
    void insertNote(Note note);

    @Query("SELECT * FROM Note")
    List<Note> getAllNotes();

    @Update
    void updateNote(Note note);
    @Delete
    void deleteNote(Note note);
}
