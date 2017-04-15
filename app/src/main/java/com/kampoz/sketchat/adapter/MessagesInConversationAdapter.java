package com.kampoz.sketchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.model.MessageObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wasili on 2017-04-15.
 */

public class MessagesInConversationAdapter extends ArrayAdapter<MessageObject> {
    private TextView chatText;
    private List<MessageObject> itemsArrayList = new ArrayList<>();
    private LinearLayout singleMessageContainer;

    public MessagesInConversationAdapter(Context context, int textViewResourceId) {
        super(context, R.layout.layout_single_message, textViewResourceId);
    }

    @Override
    public void add(MessageObject object) {          //Adds the specified object at the end of the array
        itemsArrayList.add(object);
        super.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null)
        {
            //LayoutInflater służy do dynamicznego ładowania layoutu
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //określa widok pojedynczego rzędu
            row = inflater.inflate(R.layout.layout_single_message, parent, false);
        }
        singleMessageContainer = (LinearLayout) row.findViewById(R.id.singleMessageContainer);
        MessageObject singleMessageObject = getItem(position);
        chatText = (TextView) row.findViewById(R.id.singleMessage);
        chatText.setText(singleMessageObject.message);
        chatText.setBackgroundResource(singleMessageObject.left ? R.drawable.bubble_a : R.drawable.bubble_b);
        singleMessageContainer.setGravity(singleMessageObject.left ? Gravity.LEFT : Gravity.RIGHT);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
