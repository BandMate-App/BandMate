package edu.fsu.cs.bandmate.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseUser;

import java.util.List;

import edu.fsu.cs.bandmate.Message;
import edu.fsu.cs.bandmate.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private List<Message> parseMessages;
    private List<Object> messages;
    private Context mContext;
    private String username;
    private Bitmap icon;
    private String user = ParseUser.getCurrentUser().getUsername();

    private static final int outgoing=0;
    private static final int incoming=1;


    @Override
    public int getItemViewType(int position){
        if(isMe(position))
            return outgoing;
        else
            return incoming;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ChatAdapter(Context context, String username, List<Object> messages,Bitmap icon){
        this.messages = messages;
        this.username = username;
        this.icon = icon;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private boolean isMe(int position){
        Message message = parseMessages.get(position);
        return message.getUser() != null && message.getUser().equals(username);
    }
}
