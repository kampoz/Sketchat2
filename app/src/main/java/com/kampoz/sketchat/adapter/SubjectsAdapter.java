package com.kampoz.sketchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.SubjectRealm;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter {

  public interface OnSubjectItemSelectedListener {
    void onItemSelect(long id);
    void onEditItem(SubjectRealm subjectRealm);
  }

  private OnSubjectItemSelectedListener onSubjectItemSelectedListener;
  private RecyclerView recyclerView;
  private ArrayList<SubjectRealm> subjectsList;
  private boolean areEditButtonsShown;
  private Context context;

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

  public SubjectsAdapter(ArrayList<SubjectRealm> subjectsList, RecyclerView recyclerView) {
    this.subjectsList = subjectsList;
    this.recyclerView = recyclerView;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.layout_single_subject, viewGroup, false);
    view.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        /*** Wejscie w rozmowę zrobić
         * Tu chyba sie jednak nie da bo nier moxzna pobrac subject id;
         * i to nie position, a kokreslenie widoku? ***/
      }
    });
    return new SubjectsAdapter.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
    final TextView tvSubject = ((MyViewHolder) viewHolder).tvSubject;
    final Button bEditSubject = ((MyViewHolder) viewHolder).bEditSubject;
    final TextView tvSubjectMembersNumber = ((MyViewHolder) viewHolder).tvSubjectMembersNumber;
    final ImageView ivSubjectMembersImage = ((MyViewHolder) viewHolder).ivSubjectMembersImage;
    SubjectRealm currentSubject = subjectsList.get(position);
    final long currentSubjectId = currentSubject.getId();
    if (areEditButtonsShown) {
      bEditSubject.setVisibility(View.VISIBLE);
      ivSubjectMembersImage.setVisibility(View.INVISIBLE);
      tvSubjectMembersNumber.setVisibility(View.INVISIBLE);
    } else {
      bEditSubject.setVisibility(View.INVISIBLE);
      ivSubjectMembersImage.setVisibility(View.VISIBLE);
      tvSubjectMembersNumber.setVisibility(View.VISIBLE);
    }
    ((MyViewHolder) viewHolder).tvSubject.setText(subjectsList.get(position).getSubject());
    ((MyViewHolder) viewHolder).itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if(!areEditButtonsShown){
          onSubjectItemSelectedListener.onItemSelect(subjectsList.get(position).getId());
        }
      }
    });

    bEditSubject.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                        onSubjectItemSelectedListener.onEditItem(subjectsList.get(position));
                                      }
                                    }
    );
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

  public void setOnSubjectItemSelectedListener(
      OnSubjectItemSelectedListener onSubjectItemSelectedListener) {
    this.onSubjectItemSelectedListener = onSubjectItemSelectedListener;
  }
}
