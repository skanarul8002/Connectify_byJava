package com.anarul.skwhatsapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anarul.skwhatsapp.Models.MessageModel;
import com.anarul.skwhatsapp.R;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;
    String senderRoom;
    String receverRoom;

    int SENDER_VIEW_TYPE=1;     //identify view holder send msg or receive msg or record etc.
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==SENDER_VIEW_TYPE) {
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.sample_reciver,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }
        }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      MessageModel messageModel=messageModels.get(position);

      holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
          @Override
          public boolean onLongClick(View view) {
              new AlertDialog.Builder(context).setTitle("Delete")
                      .setMessage("Are you Sure you Want to Delete This Message")
                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              FirebaseDatabase database=FirebaseDatabase.getInstance();
                              String senderRoom=FirebaseAuth.getInstance().getUid()+recId;
                              database.getReference().child("Chats")
                                      .child(senderRoom).child(messageModel.getMessageId()).setValue(null);
                          }
                      }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                  }
              }).show();
              return false;
          }
      });
       int reaction[]=new int[]{
                R.drawable.laughing,
                R.drawable.like,
                R.drawable.dislike,
                R.drawable.pressure,
                R.drawable.shocked,
                R.drawable.smile,
                R.drawable.thinking,
                R.drawable.hug,
                R.drawable.crying,
                R.drawable.love,
                R.drawable.angry
       };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reaction)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(holder.getClass()==SenderViewHolder.class) {
                SenderViewHolder viewHolder = (SenderViewHolder) holder;
                viewHolder.feeling.setImageResource(reaction[pos]);
                viewHolder.feeling.setVisibility(View.VISIBLE);
            }
            else {
                ReceiverViewHolder viewHolder=(ReceiverViewHolder)holder;
                viewHolder.feeling.setImageResource(reaction[pos]);
                viewHolder.feeling.setVisibility(View.VISIBLE);
            }
            return true; // true is closing popup, false is requesting a new selection

        });

        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.senderMsg.setText(messageModel.getMessage());
         // ((SenderViewHolder)holder).senderTime.time(messageModel.getTimestamp());
            viewHolder.senderMsg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
      }
      else{
          ReceiverViewHolder viewHolder=(ReceiverViewHolder)holder;
          viewHolder.receiverMsg.setText(messageModel.getMessage());
         //((ReceiverViewHolder)holder).receiverTime.s(messageModel.getTimestamp());
            viewHolder.receiverMsg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
      }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg,senderTime;
        ImageView feeling;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);
            feeling=itemView.findViewById(R.id.feeling);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg,receiverTime;
        ImageView feeling;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg=itemView.findViewById(R.id.reciverText);
            receiverTime=itemView.findViewById(R.id.reciverTime);
            feeling=itemView.findViewById(R.id.feeling);
        }
    }
}
