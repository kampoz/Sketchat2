package com.kampoz.sketchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.SubjectRealm;

import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-16.
 */

public class SubjectsAdapter extends RecyclerView.Adapter{

    private RecyclerView recyclerView;
    private ArrayList<SubjectRealm> subjectsList;
    private boolean areEditButtonsShown;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSubject;
        private Button bEditSubject;
        private TextView tvSubjectMembersNumber;
        private ImageView ivSubjectMembersImage;


        public MyViewHolder(View view) {
            super(view);
            tvSubject = (TextView) view.findViewById(R.id.tvSubject);
            bEditSubject = (Button) view.findViewById(R.id.bEditSubject);
            tvSubjectMembersNumber = (TextView) view.findViewById(R.id.tvSubjectMembersNumber);
            ivSubjectMembersImage = (ImageView) view.findViewById(R.id.ivSubjectMembersImage);

        }
    }

    public SubjectsAdapter(ArrayList<SubjectRealm> subjectsList, RecyclerView recyclerView){
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
        return new SubjectsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final TextView tvSubject = ((MyViewHolder)viewHolder).tvSubject;
        final Button bEditSubject = ((MyViewHolder)viewHolder).bEditSubject;
        final TextView tvSubjectMembersNumber = ((MyViewHolder)viewHolder).tvSubjectMembersNumber;
        final ImageView ivSubjectMembersImage = ((MyViewHolder)viewHolder).ivSubjectMembersImage;
        if(areEditButtonsShown){
            bEditSubject.setVisibility(View.VISIBLE);
            ivSubjectMembersImage.setVisibility(View.INVISIBLE);
            tvSubjectMembersNumber.setVisibility(View.INVISIBLE);
        }else{
            bEditSubject.setVisibility(View.INVISIBLE);
            ivSubjectMembersImage.setVisibility(View.VISIBLE);
            tvSubjectMembersNumber.setVisibility(View.VISIBLE);
        }


        ((MyViewHolder)viewHolder).tvSubject.setText(subjectsList.get(position).getSubject());
    }

    @Override
    public int getItemCount() {
        return subjectsList.size();
    }

    public ArrayList<SubjectRealm> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(ArrayList<SubjectRealm> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public void setAreEditButtonsShown(boolean areEditButtonsShown) {
        this.areEditButtonsShown = areEditButtonsShown;
    }
}
