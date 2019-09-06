package com.submissionform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.submissionform.Model.Notes;
import com.submissionform.Model.Resources;
import com.submissionform.R;

import java.util.ArrayList;

public class ReourcesAdapter extends BaseAdapter {

    private ArrayList<Resources> resources;
    private Context context;


    public ReourcesAdapter(Context context, ArrayList<Resources> resources){
        this.context = context;
        this.resources = resources;

    }
    @Override
    public int getCount() {
        return resources.size();
    }

    @Override
    public Object getItem(int position) {
        return resources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.custom_notes_row, null);//set layout for displaying i
        Resources obj = resources.get(position);
        TextView notesTitle = (TextView)view.findViewById(R.id.notesTitle);
        TextView notesSubject = (TextView)view.findViewById(R.id.notesSubject);

        notesTitle.setText(obj.getResourceLink());
        notesSubject.setText(obj.getResourceLinkDescription());
        return  view;
    }
}
