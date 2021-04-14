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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.bandmate.Conversation;
import edu.fsu.cs.bandmate.Message;
import edu.fsu.cs.bandmate.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{
    private ArrayList<Message> parseMessages;
    private Conversation conversation;
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
        try {
            if(isMe(position))
                return outgoing;
            else
                return incoming;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
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
            body = (TextView) itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);

        }

        @Override
        void bindMessage(Message message) {
            iv_other.setImageBitmap(icon);
            body.setText(message.getbody());
            name.setText(message.getUser());
        }
    }

    public class OutgoingMessageViewHolder extends ChatViewHolder{


        public OutgoingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_me = (ImageView) itemView.findViewById(R.id.ivProfileMe);
            body = (TextView) itemView.findViewById(R.id.tvBody);

        }

        @Override
        void bindMessage(Message message) {
            body.setText(message.getbody());
            iv_me.setImageBitmap(icon);

        }
    }

    public ChatAdapter(Context context, String username, Conversation conversation,Bitmap icon) throws ParseException {
        this.conversation = conversation;
        this.username = username;
        this.icon = icon;
        parseMessages = (ArrayList<Message>) conversation.fetchIfNeeded().get(Conversation.KEY_MESSAGEOBJECT);
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
        return parseMessages.size();
    }

    private boolean isMe(int position) throws ParseException {
        Message message = parseMessages.get(position);
        return message.fetchIfNeeded().get(Message.KEY_USER).toString().replace("\"", "") != null && message.fetchIfNeeded().get(Message.KEY_USER).toString().replace("\"", "").equals(username);
    }
}
