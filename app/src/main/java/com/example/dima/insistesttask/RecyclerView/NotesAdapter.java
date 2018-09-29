package com.example.dima.insistesttask.RecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dima.insistesttask.R;
import com.example.dima.insistesttask.Room.Note;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private ArrayList<Note> data;
    private OnItemClickListener listener;

    public NotesAdapter(ArrayList<Note> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        final NotesViewHolder vh = new NotesViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, vh.getAdapterPosition());
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder vh, int i) {
        vh.noteNameView.setText(data.get(vh.getAdapterPosition()).getNoteName());
        if(data.get(vh.getAdapterPosition()).getColor() != -1) {
            vh.cardView.setCardBackgroundColor(data.get(vh.getAdapterPosition()).getColor());
            vh.noteNameView.setBackgroundColor(data.get(vh.getAdapterPosition()).getColor());
        }

    }

    @Override
    public int getItemCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        }else {
            return 0;
        }
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView noteNameView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            noteNameView = itemView.findViewById(R.id.notes_name);
        }
    }
}
