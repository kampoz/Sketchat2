package com.kampoz.sketchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.dao.UserRealmLocalDao;
import com.kampoz.sketchat.realm.MessageRealm;
import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-15.
 */

public class ConversationAdapter extends RecyclerView.Adapter {

  private RecyclerView recyclerView;
  private ArrayList<MessageRealm> messages; // = myGenerator.generateMessagesArrayList(10);
  private UserRealmLocalDao userRealmLocalDao;
  int TYPE_RIGHT = 0;
  int TYPE_LEFT = 1;

  private class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tvMessageText;
    //private LinearLayout singleMessageContainer;
    public MyViewHolder(View view) {
      super(view);
      tvMessageText = (TextView) view.findViewById(R.id.singleMessage);
    }
  }

  public ConversationAdapter(ArrayList<MessageRealm> messages, RecyclerView recyclerView) {
    this.messages = messages;
    this.recyclerView = recyclerView;
  }


  //met. okresla typ layoutu dla pojedynczego row. Mozna tu ustawic dowolna liczbe layoputów.
  @Override
  public int getItemViewType(int position) {
    MessageRealm messageObject = messages.get(position);
    userRealmLocalDao = new UserRealmLocalDao();
    //int type =  (messageObject.getUserId() == userRealmLocalDao.getCurrentLoginUser().getId())? TYPE_RIGHT : TYPE_LEFT;
    int type;
    if (messageObject.getUserId() == userRealmLocalDao.getCurrentLoginUser().getId()){
      type = TYPE_RIGHT;
    }else{
      type = TYPE_LEFT;
    }
    userRealmLocalDao.closeRealmInstance();
    return type;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view;
    if (i == TYPE_LEFT) {
      view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_message_left, viewGroup, false);
    } else {
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
    ((MyViewHolder) viewHolder).tvMessageText.setText(messages.get(position).getMessageText());
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }
}
