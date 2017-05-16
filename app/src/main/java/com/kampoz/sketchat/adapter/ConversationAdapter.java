package com.kampoz.sketchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.MessageRealm;

import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-15.
 */

public class ConversationAdapter extends RecyclerView.Adapter {

    private RecyclerView recyclerView;
    private ArrayList<MessageRealm> messages; // = myGenerator.generateMessagesArrayList(10);

    int TYPE_RIGHT = 0;
    int TYPE_LEFT = 1;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessageText;
        private LinearLayout singleMessageContainer;

        public MyViewHolder(View view) {
            super(view);
            tvMessageText = (TextView) view.findViewById(R.id.singleMessage);
        }
    }

    public ConversationAdapter(ArrayList<MessageRealm> messages, RecyclerView recyclerView){
        this.messages = messages;
        this.recyclerView = recyclerView;
    }


    //met. okresla typ layoutu dla pojedynczego row. Mozna tu ustawic dowolna liczbe layoputów.
    @Override
    public int getItemViewType(int position) {
        MessageRealm messageObject = messages.get(position);
        return messageObject.isLeft() ? TYPE_LEFT : TYPE_RIGHT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;

        if(i == TYPE_LEFT)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_left, viewGroup, false);
        }
        else
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_right, viewGroup, false);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 /***  tu pobrac dane na temat wybragej konkretnej rozmowy, aby ja uruchomić  ***/
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        ((MyViewHolder)viewHolder).tvMessageText.setText(messages.get(position).getStringMessageText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}