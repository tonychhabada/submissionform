package com.submissionform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.submissionform.Model.NewNote;
import com.submissionform.Model.Notes;
import com.submissionform.R;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends BaseAdapter {

    private List<NewNote> notes;
    private Context context;


    public NotesAdapter(Context context, List<NewNote> notes){
        this.context = context;
        this.notes = notes;

    }
    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.custom_notes_row, null);//set layout for displaying i
        NewNote obj = notes.get(position);
        TextView notesTitle = (TextView)view.findViewById(R.id.notesTitle);
        TextView notesSubject = (TextView)view.findViewById(R.id.notesSubject);

        notesTitle.setText(obj.notesTitle);
        notesSubject.setText(obj.note);
        return  view;
    }
}
