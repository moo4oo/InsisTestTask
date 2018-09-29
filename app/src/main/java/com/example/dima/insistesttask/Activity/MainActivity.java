package com.example.dima.insistesttask.Activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dima.insistesttask.R;
import com.example.dima.insistesttask.RecyclerView.NotesAdapter;
import com.example.dima.insistesttask.RecyclerView.OnItemClickListener;
import com.example.dima.insistesttask.Room.Note;
import com.example.dima.insistesttask.Room.NotesDaoAccess;
import com.example.dima.insistesttask.Room.NotesDatabase;
import com.example.dima.insistesttask.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditNotesActivity.class));
            }
        });
        recyclerView = findViewById(R.id.notes_rec_view);
        progressBar = findViewById(R.id.progress_bar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new NotesAsyncTask().execute("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void initRecView(final ArrayList<Note> notes) {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NotesAdapter adapter = new NotesAdapter(notes, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*
                Snackbar snackbar = Snackbar
                        .make(view, "Clicked " + position, Snackbar.LENGTH_LONG);

                snackbar.show();*/
                Intent intent = new Intent(MainActivity.this, EditNotesActivity.class);
                intent.putExtra(Util.EDIT_NOTES_FLAG, Util.EDIT_NOTES_FLAG);
                intent.putExtra(Util.NOTE_NAME, notes.get(position).getNoteName());
                intent.putExtra(Util.NOTE_DESCRIPTION, notes.get(position).getNoteDescription());
                intent.putExtra(Util.NOTE_ID, notes.get(position).getNoteId());
                intent.putExtra(Util.COLOR, notes.get(position).getColor());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }
    private class NotesAsyncTask extends AsyncTask<String, String, ArrayList<Note>>{

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            super.onPostExecute(notes);
            initRecView(notes);
        }

        @Override
        protected ArrayList<Note> doInBackground(String... strings) {
            NotesDatabase db = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, "Note").build();
            List<Note> notes;
            NotesDaoAccess dao = db.notesDaoAccess();
            notes = dao.getAllNotes();
            if(notes != null && notes.size()!=0) {
                return new ArrayList<>(notes);
            }else{
                /*
                for(int i = 0; i < 10; i++){
                    Note note = new Note("note " + (i+1), "note description " + i);
                    note.setColor(-1);
                    dao.insertNote(note);
                }*/
                Gson gson = new GsonBuilder().setLenient().create();
                //JsonObject json = new JsonObject();
                com.example.dima.insistesttask.Gson.NotesResponse data = gson.fromJson(getResources().getString(R.string.json_pre_populate), com.example.dima.insistesttask.Gson.NotesResponse.class);
                for(com.example.dima.insistesttask.Gson.Note note : data.getNotes()){
                    Note n = new Note(note.getNoteName(), note.getNoteDescription());
                    n.setNoteId(note.getId());
                    n.setColor(note.getColor());
                    dao.insertNote(n);
                }
                notes = dao.getAllNotes();
                return new ArrayList<>(notes);
            }
        }
        public void stop(){
            this.stop();
        }
    }
}
