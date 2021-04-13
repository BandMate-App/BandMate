package edu.fsu.cs.bandmate.adapters;

import edu.fsu.cs.bandmate.SelectedConversation;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import edu.fsu.cs.bandmate.ConversationList;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.fragments.MessagesFragment;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder>{


    Context context;
    ConversationList list;
    ArrayList<List<Object>> messages;
    ArrayList<ParseUser> matches;
    ArrayList<Bitmap> pictures;
    CardView cardview;
    MessagesFragment.MessagesHost listener;
    //messages_listener listener;

    //TODO add latest message time
    public MessageListAdapter(Context context, ArrayList<ParseUser> otherUser,
                              ArrayList<List<Object>> Messages, ArrayList<Bitmap> pictures){
        this.matches=otherUser;
        this.context = context;
        this.messages = Messages;
        this.pictures = pictures;





    }

    public void setListener(MessagesFragment.MessagesHost listener){
        this.listener = listener;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView username,recentMessage;
        ImageView profile_pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUsername);
            recentMessage = itemView.findViewById(R.id.tvMessage);
            profile_pic = itemView.findViewById(R.id.ivProfilePicture);
            cardview = itemView.findViewById(R.id.cvConversationItem);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.conversation_list_item,parent,false);
        return new MessageListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(matches.get(position).getUsername());
        holder.recentMessage.setText(messages.get(position).get(messages.get(position).size()-1).toString());
        holder.profile_pic.setImageBitmap(pictures.get(position));
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = holder.getAbsoluteAdapterPosition();
                SelectedConversation selected = new SelectedConversation(messages.get(position), matches.get(position), pictures.get(position));
                listener.onConversationClick(selected);
            }
        });

    }



    @Override
    public int getItemCount() {
        return matches.size();
    }


    
  /*  public interface messages_listener{
        public void onConversationSelected(SelectedConversation selected);
    }
*/
}
