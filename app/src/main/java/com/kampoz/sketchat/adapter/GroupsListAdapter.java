package com.kampoz.sketchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.GroupRealm;

import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-15.
 */

public class GroupsListAdapter extends RecyclerView.Adapter{

    public interface OnGroupItemSelectedListener{
        void onItemSelect(int position);
    }

    private OnGroupItemSelectedListener onGroupItemSelectedListener;
    private RecyclerView recyclerView;
    private ArrayList<GroupRealm> groupsList;
    private boolean areRadioButtonsShown;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGroupName;
        public RadioButton radioButton;

        public MyViewHolder(View view) {
            super(view);
            tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
            radioButton = (RadioButton)view.findViewById(R.id.rbDelete);
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
        final RadioButton radioButton = ((MyViewHolder)viewHolder).radioButton;
        if(areRadioButtonsShown){
            radioButton.setVisibility(View.VISIBLE);
        }else{
            radioButton.setVisibility(View.GONE);
        }
        ((MyViewHolder)viewHolder).tvGroupName.setText(groupsList.get(position).getGroupName());
        ((MyViewHolder)viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Context context = view.getContext();
//                Intent startSubjectsListActivityIntent = new Intent(context, SubjectsListActivity.class);
//                context.startActivity(startSubjectsListActivityIntent);
                //this.finish();
                if(areRadioButtonsShown){
                    if(radioButton.isChecked()){
                        radioButton.setChecked(false);
                    }else{
                        radioButton.setChecked(true);
                    }
                }

                if(!areRadioButtonsShown){
                    onGroupItemSelectedListener.onItemSelect(position);
                }

                //String groupName = groupsList.get(position).getGroupName();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public void setOnGroupItemSelectedListener(OnGroupItemSelectedListener onGroupItemSelectedListener) {
        this.onGroupItemSelectedListener = onGroupItemSelectedListener;
    }

    public boolean isAreRadioButtonsShown() {
        return areRadioButtonsShown;
    }

    public void setAreRadioButtonsShown(boolean areRadioButtonsShown) {
        this.areRadioButtonsShown = areRadioButtonsShown;
    }
}
