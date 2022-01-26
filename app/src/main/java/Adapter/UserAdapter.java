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

import java.util.List;

import Model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Users> mUsers;
private final boolean isChat;
    public UserAdapter(Context mContext, List<Users> mUsers ,boolean isChat){
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat= isChat;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);

        return new UserAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.profilepic.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profilepic);
        }
        if(isChat){
            if(user.getStatus().equals("Online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }
            else
            {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }

        }
        else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profilepic;
        private final ImageView img_on;
        private final ImageView img_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username =itemView.findViewById(R.id.usernamee);
            profilepic = itemView.findViewById(R.id.profile_image);
            img_on= itemView.findViewById(R.id.img_on);
            img_off=itemView.findViewById(R.id.img_off);



        }
    }

}

