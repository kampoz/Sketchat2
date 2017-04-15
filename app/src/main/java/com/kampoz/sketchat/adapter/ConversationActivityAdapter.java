package com.kampoz.sketchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.helper.MyRandomValuesGenerator;
import com.kampoz.sketchat.model.MessageObject;

import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-15.
 */

public class ConversationActivityAdapter extends RecyclerView.Adapter {

    private ArrayList<MessageObject> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    MyRandomValuesGenerator myGenerator = new MyRandomValuesGenerator();

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessageText;
        private LinearLayout singleMessageContainer;


        public MyViewHolder(View view) {
            super(view);
            tvMessageText = (TextView) view.findViewById(R.id.singleMessage);
        }
    }

    public ConversationActivityAdapter(ArrayList<MessageObject> messages, RecyclerView recyclerView){
        this.messages = messages;
        this.recyclerView = recyclerView;
    }

    public ConversationActivityAdapter(){

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view;
        final boolean isLeft = myGenerator.getrandomBoolean();
        if(isLeft) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_left, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_right, viewGroup, false);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Context context = view.getContext();
                //int intClickedPosition = recyclerView.getChildAdapterPosition(view);
                //MessageObject messageObject = messages.get(intClickedPosition);

                //tu pobrac dane na temat wybragej konkretnej rozmowy, aby ja uruchomić

            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        //Prawidłowe gettery
        //final MessageObject messageObject = messages.get(position);
        //final boolean isLeft = messageObject.isLeft();
        final boolean isLeft = myGenerator.getrandomBoolean();
        ((MyViewHolder)viewHolder).tvMessageText.setText(myGenerator.getRandomText());

        if(isLeft){

        }
        else{

        }
    }

    @Override
    public int getItemCount() {
        //return messages.size();
        return 30;
    }
}
