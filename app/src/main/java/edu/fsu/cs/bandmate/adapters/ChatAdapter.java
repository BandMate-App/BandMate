package edu.fsu.cs.bandmate.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import edu.fsu.cs.bandmate.Message;
import edu.fsu.cs.bandmate.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private List<Message> parseMessages;
    private List<Object> messages;
    private Context mContext;
    private String username;
    private Bitmap icon;
    private ImageView iv_other;
    private String user = ParseUser.getCurrentUser().getUsername();
    private TextView body;
    private ImageView iv_me;
    private TextView name;

    private static final int outgoing=0;
    private static final int incoming=1;


    @Override
    public int getItemViewType(int position){
        if(isMe(position))
            return outgoing;
        else
            return incoming;
    }

    public abstract class  ChatViewHolder extends RecyclerView.ViewHolder{

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        abstract void bindMessage(Message message);
    }

    public class IncomingMessageViewHolder extends ChatViewHolder{

        public IncomingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_other = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            body = (TextView) itemView.findViewById(R.id.tvMessage);
            name = (TextView)itemView.findViewById(R.id.tvName);

        }

        @Override
        void bindMessage(Message message) {
            iv_other = (ImageView) itemView.findViewById(R.id.ivProfileOther);
            body = (TextView) itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);
        }
    }

    public class OutgoingMessageViewHolder extends ChatViewHolder{


        public OutgoingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_me = (ImageView) itemView.findViewById(R.id.ivProfileOther);
            body = (TextView) itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);

        }

        @Override
        void bindMessage(Message message) {
            body.setText(message.getbody());
            name.setText(message.getUser());

        }
    }

    public ChatAdapter(Context context, String username, List<Object> messages,Bitmap icon){
        this.messages = messages;
        this.username = username;
        this.icon = icon;
    }

    @NotNull
    @Override public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if(viewType == incoming){
            View contactView = inflater.inflate(R.layout.message_incoming,parent,false);
            return new IncomingMessageViewHolder(contactView);
        }
        else if(viewType == outgoing){
            View contactView = inflater.inflate(R.layout.message_outgoing,parent,false);
            return new OutgoingMessageViewHolder(contactView);
        }
        else{
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        Message message = parseMessages.get(position);
        holder.bindMessage(message);
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