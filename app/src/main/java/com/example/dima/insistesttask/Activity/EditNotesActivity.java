package com.example.dima.insistesttask.Activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.dima.insistesttask.R;
import com.example.dima.insistesttask.Room.Note;
import com.example.dima.insistesttask.Room.NotesDaoAccess;
import com.example.dima.insistesttask.Room.NotesDatabase;
import com.example.dima.insistesttask.Util;

import top.defaults.colorpicker.ColorPickerPopup;

public class EditNotesActivity extends AppCompatActivity {
    private int noteId;
    private String noteName;
    private String noteDescription;
    private EditText editName;
    private EditText editDescription;
    private Button pickColorBtn;
    private Button saveBtn;
    private ConstraintLayout layout;
    private int color = -1;
    private final Integer MODE_UPDATE = 0;
    private final Integer MODE_INSERT = 1;
    private Integer MODE = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        android.app.ActionBar bar = getActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);
        editName = findViewById(R.id.edit_note_name);
        editDescription = findViewById(R.id.edit_note_description);
        layout = findViewById(R.id.edit_layout);
        pickColorBtn = findViewById(R.id.color_btn);

        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(MODE);
            }
        });
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(Util.EDIT_NOTES_FLAG)) {
                editNotesMode(extras);
                MODE = MODE_UPDATE;

            } else {
                newNoteMode();
                MODE = MODE_INSERT;

            }
        } else {
            newNoteMode();
            MODE = MODE_INSERT;

        }
        pickColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(v, "Для выбора цвета нажмите кнопку Choose в верхнем правом углу диалогового окна", Snackbar.LENGTH_LONG);
                snackbar.show();
                final View view = v;
                final ActionBar actionBar = getSupportActionBar();
                int initColor = Color.RED;
                if(color != -1){
                    initColor = color;
                }else{
                    color = initColor;
                }
                new ColorPickerPopup.Builder(EditNotesActivity.this)
                        .initialColor(initColor) // Set initial color
                        .enableAlpha(true) // Enable alpha slider or not
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(view, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                EditNotesActivity.this.color = color;
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {
                                if(actionBar != null)
                                    actionBar.setBackgroundDrawable(new ColorDrawable(color));

                            }
                        });
            }
        });
        Button deleteBtn = findViewById(R.id.delete_btn);
        if(MODE == MODE_UPDATE){
            deleteBtn.setVisibility(View.VISIBLE);
        }else{
            deleteBtn.setVisibility(View.GONE);
        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(noteName, noteDescription);
                note.setColor(color);
                note.setNoteId(noteId);
                new DeleteNoteAsyncTask().execute(note);
            }
        });
    }
    private void saveData(Integer mode){
        SaveNoteAsyncTask task = new SaveNoteAsyncTask();
        task.execute(mode);
    }
    private class SaveNoteAsyncTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Snackbar snackbar = Snackbar
                    .make(saveBtn, "Данные успешно сохранены", Snackbar.LENGTH_LONG);

            snackbar.show();
        }

        @Override
        protected Void doInBackground(Integer... mode) {
            NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "Note").build();
            NotesDaoAccess dao = db.notesDaoAccess();
            Note note = new Note(editName.getText().toString(), editDescription.getText().toString());
            note.setColor(color);
            note.setNoteId(noteId);
            switch (mode[0]){
                case 0:
                    dao.updateNote(note);
                    break;
                case 1:
                    dao.insertNote(note);
                    break;
            }

            return null;
        }
    }
    private class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Snackbar snackbar = Snackbar
                    .make(saveBtn, "Заметка успешно удалена", Snackbar.LENGTH_LONG);

            snackbar.show();

        }

        @Override
        protected Void doInBackground(Note... notes) {
            NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "Note").build();
            NotesDaoAccess dao = db.notesDaoAccess();
            dao.deleteNote(notes[0]);
            return null;
        }
    }

    private void editNotesMode(Bundle extras){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        noteName = extras.getString(Util.NOTE_NAME);
        noteDescription = extras.getString(Util.NOTE_DESCRIPTION);
        editName.setText(noteName);
        editDescription.setText(noteDescription);
        noteId = extras.getInt(Util.NOTE_ID);
        color = extras.getInt(Util.COLOR);
        ActionBar bar = getSupportActionBar();
        if(bar != null){
            if(color != -1)
            bar.setBackgroundDrawable(new ColorDrawable(color));
        }

    }
    private void newNoteMode(){
        editName.setHint("Enter note's name");
        editDescription.setHint("Enter note's description");

    }



}
