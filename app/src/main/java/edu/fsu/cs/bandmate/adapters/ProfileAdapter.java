package edu.fsu.cs.bandmate.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.List;

import edu.fsu.cs.bandmate.Profile;
import edu.fsu.cs.bandmate.R;
import edu.fsu.cs.bandmate.fragments.ProfileFragment;

import static edu.fsu.cs.bandmate.MainActivity.TAG;

public class ProfileAdapter extends CardStackView.Adapter<ProfileAdapter.ViewHolder> {
    List<Profile> profileList;
    Context context;

    /**
     * Constructor for ProfileAdapter
     *
     * @param profileList
     * @param context
     */
    public ProfileAdapter(List<Profile> profileList, Context context) {
        this.profileList=profileList;
        this.context=context;
    }


    /**
     * Inflates feed_item to hold
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.feed_item,parent,false);
        return new ViewHolder(v);
    }

    /**
     * Binds the element to the holder.
     * Sets an onclicklistener to load a profile fragment
     * for the element that was selected.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile profile = profileList.get(position);
        holder.bind(profile);
        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onClick: profile info: "+profile.getName());
            Bundle bundle = new Bundle();
            bundle.putString("profileUsername", profile.getUser().getUsername());
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(bundle);

            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentFrame, profileFragment).commit();
        });

    }

    private void fragmentProfile(Profile profile){

    }

    /**
     * returns the a count that represents the number of
     * profiles in the profileList
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return profileList.size();
    }

    /**
     * Loads a complete list of profiles into the profile list and
     * triggers event for dataset change.
     * @param profiles
     */
    public void addAll(List<Profile> profiles) {
        profileList.addAll(profiles);
        notifyDataSetChanged();
    }

    /**
     * Declares class for holding the elements of a profile in the feed.
     * Defines how the profiles are bound in the holder.
     */
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivProfilePic;
        TextView tvName;
        TextView tvGenre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic=itemView.findViewById(R.id.ivProfilePic);
            tvName = itemView.findViewById(R.id.tvName);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }

        public void bind(Profile profile) {
            if (profile == null) return;
            tvName.setText(profile.getName());
            tvGenre.setText(profile.getGenre());

            Glide.with(context)
                    .load(profile.getImage().getUrl())
                    .apply(new RequestOptions()
                            .transform(new CenterCrop(), new RoundedCorners(16)))
                    .into(ivProfilePic);
        }
    }
}
