package com.kampoz.sketchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.GroupRealm;

import java.util.ArrayList;

public class GroupsListAdapter extends RecyclerView.Adapter{

    public interface OnGroupItemSelectedListener{
        void onItemSelect(int position);
        void onEditItem(GroupRealm groupRealm);
    }

    private OnGroupItemSelectedListener onGroupItemSelectedListener;
    private RecyclerView recyclerView;
    private ArrayList<GroupRealm> groupsList;
    private boolean areEditButtonsShown;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGroupName;
        public Button bEditGroup;
        private TextView tvGroupSubjectsNumber;
        private ImageView ivPencil;

        public MyViewHolder(View view) {
            super(view);
            tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
            bEditGroup = (Button)view.findViewById(R.id.bEditGroup);
            tvGroupSubjectsNumber = (TextView) view.findViewById(R.id.tvGroupSubjectsNumber);
            ivPencil = (ImageView)view.findViewById(R.id.ivPencil);
        }
    }

    public GroupsListAdapter(ArrayList<GroupRealm> groupsList, RecyclerView recyclerView){
        this.groupsList = groupsList;
        this.recyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_single_group, viewGroup, false);
        return new GroupsListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final Button bEditGroup = ((MyViewHolder)viewHolder).bEditGroup;
        final TextView tvGroupSubjectsNumber = ((MyViewHolder)viewHolder).tvGroupSubjectsNumber;
        final ImageView ivPencil = ((MyViewHolder)viewHolder).ivPencil;
        if(areEditButtonsShown){
            bEditGroup.setVisibility(View.VISIBLE);
            tvGroupSubjectsNumber.setVisibility(View.INVISIBLE);
            ivPencil.setVisibility(View.INVISIBLE);
        }else{
            bEditGroup.setVisibility(View.GONE);
            tvGroupSubjectsNumber.setVisibility(View.VISIBLE);
            ivPencil.setVisibility(View.VISIBLE);
        }
        ((MyViewHolder)viewHolder).tvGroupName.setText(groupsList.get(position).getGroupName());
        final String groupName = ((MyViewHolder)viewHolder).tvGroupName.getText().toString();
        ((MyViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!areEditButtonsShown){
                    onGroupItemSelectedListener.onItemSelect(position);
                }
                //String groupName = groupsList.get(position).getGroupName();
            }
        });
        bEditGroup.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      onGroupItemSelectedListener.onEditItem(groupsList.get(position));
                  }
              }
        );
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public void setOnGroupItemSelectedListener(OnGroupItemSelectedListener onGroupItemSelectedListener) {
        this.onGroupItemSelectedListener = onGroupItemSelectedListener;
    }

    public boolean isAreEditButtonsShown() {
        return areEditButtonsShown;
    }

    public void setAreEditButtonsShown(boolean areEditButtonsShown) {
        this.areEditButtonsShown = areEditButtonsShown;
    }
}
