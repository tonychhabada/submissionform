package com.submissionform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.submissionform.Model.InputDataModel;
import com.submissionform.Model.Lead;
import com.submissionform.Model.Notes;
import com.submissionform.R;

import java.util.ArrayList;
import java.util.List;

public class FormsAdapter extends BaseAdapter {

    private ArrayList<Lead> forms;
    private Context context;


    public FormsAdapter(Context context, ArrayList<Lead> forms){
        this.context = context;
        this.forms = forms;

    }
    @Override
    public int getCount() {
        return forms.size();
    }

    @Override
    public Object getItem(int position) {
        return forms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.custom_leads_row, null);//set layout for displaying i
        Lead obj = forms.get(position);
        TextView leadsFullName = (TextView)view.findViewById(R.id.leadsFullName);
        TextView leadsEmail = (TextView)view.findViewById(R.id.leadsEmail);
        TextView leadsCreated = (TextView)view.findViewById(R.id.leadsDate);
        leadsCreated.setText(obj.getCreatedDate());
        leadsFullName.setText(obj.getFullName());
        leadsEmail.setText(obj.getEmail());
        return view;
    }
}
