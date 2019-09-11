package com.submissionform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.submissionform.Model.Documents;
import com.submissionform.R;

import java.util.ArrayList;

public class MyDocumentsAdapter extends BaseAdapter {

    ArrayList<Documents> documents;
    Context context;
    public MyDocumentsAdapter(Context context,ArrayList<Documents> documents) {
        this.documents = documents;
        this.context = context;

    }

    @Override
    public int getCount() {
        return documents.size();
    }

    @Override
    public Object getItem(int position) {
        return documents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.custom_documents_row, null);//set layout for displaying i
        Documents obj = documents.get(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);

        Picasso.get().load(obj.getDocumentURL()).into(imageView);

        return view;
    }
}
