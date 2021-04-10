package edu.fsu.cs.bandmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.fsu.cs.bandmate.fragments.MessagesFragment;
import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;

class MessagesFragmentAdapter extends RecyclerView.Adapter<MessagesFragmentAdapter.ViewHolder> {
    List<Profile> conversationList;
    Context context;

    //TODO
    public MessagesFragmentAdapter(List<Profile> conversationList, Context context) {
        this.conversationList=conversationList;
        this.context=context;
    }
    //TODO
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feed_item,parent,false);
        return new ViewHolder(v);
    }

    //TODO
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile messages = conversationList.get(position);
        holder.bind(messages);

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
    //TODO
    public void addAll(List<Profile> messagess) {
        conversationList.addAll(messagess);
        notifyDataSetChanged();
    }
    //TODO
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMessagesFragmentPic;
        TextView tvName;
        TextView tvGenre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        //TODO
        public void bind(Profile messages) {
            if (messages == null) return;

        }
    }
}
