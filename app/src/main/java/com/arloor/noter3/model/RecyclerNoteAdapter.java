package com.arloor.noter3.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arloor.noter3.MainActivity;
import com.arloor.noter3.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by arloor on 2017/2/15.
 */

public class RecyclerNoteAdapter extends RecyclerView.Adapter<RecyclerNoteAdapter.ViewHolder>{

    private List<Note> mNoteList;
    private static Note noteLastClicked;

    public static Note getNoteLastClicked() {
        return noteLastClicked;
    }

    public static void setNoteLastClicked(Note noteLastClicked) {
        RecyclerNoteAdapter.noteLastClicked = noteLastClicked;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View noteView;
        TextView timeView;
        TextView themeView;

        public ViewHolder(View itemView) {
            super(itemView);
            noteView=itemView;
            timeView=(TextView)itemView.findViewById(R.id.note_time);
            themeView=(TextView)itemView.findViewById(R.id.note_theme);
        }
    }

    public RecyclerNoteAdapter(List<Note> noteList) {
        mNoteList=noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.noteView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                v.setActivated(true);

                int position=holder.getAdapterPosition();
                noteLastClicked=mNoteList.get(position);
                EditText contentEdit=(EditText)holder.noteView.getRootView().findViewById(R.id.content);
                contentEdit.setText(noteLastClicked.getContent());
                contentEdit.setHint("已选择"+noteLastClicked.getTime()+"的便签");
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note=mNoteList.get(position);
        holder.timeView.setText(note.getTime());
        holder.themeView.setText(note.getTheme());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }
}
