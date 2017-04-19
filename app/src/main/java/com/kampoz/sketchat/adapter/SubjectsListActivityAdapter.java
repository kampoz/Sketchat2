package com.kampoz.sketchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.model.SubjectModel;

import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-16.
 */

public class SubjectsListActivityAdapter extends RecyclerView.Adapter{

    private RecyclerView recyclerView;
    private ArrayList<SubjectModel> subjectsList;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSubject;

        public MyViewHolder(View view) {
            super(view);
            tvSubject = (TextView) view.findViewById(R.id.tvSubject);
        }
    }

    public SubjectsListActivityAdapter(ArrayList<SubjectModel> subjectsList, RecyclerView recyclerView){
        this.subjectsList = subjectsList;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View   view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_single_subject, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*** Wejscie w rozmowę zrobić  ***/
            }
        });
        return new SubjectsListActivityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        ((MyViewHolder)viewHolder).tvSubject.setText(subjectsList.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public ArrayList<SubjectModel> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(ArrayList<SubjectModel> subjectsList) {
        this.subjectsList = subjectsList;
    }
}
