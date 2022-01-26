package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TheDen.TalkToMe.MessageActivity;
import com.TheDen.TalkToMe.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Model.Chat;
import Model.Users;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    private final Context mContext;
    private final List<Chat> mChat;
    private final String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;


    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);

            return new MessageAdapter.ViewHolder(view);

        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);

        return new MessageAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
     Chat chat  = mChat.get(position);
     holder.show_message.setText(chat.getMessage());

     if (imageurl.equals("default")){
         holder.profilepic.setImageResource(R.mipmap.ic_launcher);

     }
     else{
         Glide.with(mContext).load(imageurl).into(holder.profilepic);
     }

     if(position == mChat.size()-1){
         if(chat.isIsseen()){
             holder.txt_seeen.setText("Seen");
         }
         else{
             holder.txt_seeen.setText("Delivered");
         }

         }else
     {
         holder.txt_seeen.setVisibility(View.GONE);
     }
    }

    @Override
    public int getItemCount() {

        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profilepic;
        public TextView txt_seeen;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message =itemView.findViewById(R.id.show_messagee);
            profilepic = itemView.findViewById(R.id.profile_image);
            txt_seeen = itemView.findViewById(R.id.txt_seen);


        }
    }


    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}

