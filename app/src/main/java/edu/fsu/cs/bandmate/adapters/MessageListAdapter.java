package edu.fsu.cs.bandmate.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder>{

    String [] users;
    String[] recentMessages;
    int[] profilePictures;
    Context context;

    //TODO add latest message time
    public MessageListAdapter(Context context, String []otherUser, String[] lastMessage, int image[]){
        this.users=otherUser;
        this.context = context;
        this.recentMessages = lastMessage;
        this.profilePictures = image;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MessageListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
