package com.seluhadu.shchat.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.models.Photo;
import com.seluhadu.shchat.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ItemHolder> {
    private static final String TAG = "HomeAdapter";
    private Context mContext;
    private ArrayList<Photo> posts;
    private FirebaseFirestore mFireBaseFireStore;

    public HomeAdapter(Context mContext, ArrayList<Photo> posts) {
        this.mContext = mContext;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_home_recycler_view, parent, false);
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        return new ItemHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        //profile
        getProfileAndName(holder, position);
        //image posted
        Photo photo = posts.get(position);
        Glide.with(mContext).load(photo.getImageUrl()).into(holder.mPostImage);
        holder.mThumpsUp.setOnClickListener(v -> {

        });
        holder.mThumpsDown.setOnClickListener(v -> {

        });
        holder.mMore.setOnClickListener(v -> {

        });
        holder.mComment.setOnClickListener(v -> {

        });
        holder.mDownload.setOnClickListener(v -> {

        });

    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private CircleImageView mProfile;
        private ImageView mPostImage;
        private ImageButton mThumpsUp;
        private ImageButton mThumpsDown;
        private ImageButton mDownload;
        private ImageButton mComment;
        private ImageButton mMore;
        private TextView mUserName;
        private TextView mTimeUpload;
        User user = new User();

        ItemHolder(View itemView) {
            super(itemView);
            this.mProfile = itemView.findViewById(R.id.user_profile);
            this.mPostImage = itemView.findViewById(R.id.image);
            this.mUserName = itemView.findViewById(R.id.user_name);
            this.mTimeUpload = itemView.findViewById(R.id.date_and_time);
            this.mDownload = itemView.findViewById(R.id.download);
            this.mComment = itemView.findViewById(R.id.comment);
            this.mMore = itemView.findViewById(R.id.more_horizontal);
            this.mThumpsUp = itemView.findViewById(R.id.thumps_up);
            this.mThumpsDown = itemView.findViewById(R.id.thumps_down);
//            this.mUserName.setText(user.getUserName());
        }
    }
    // get user display name and profile image from User.class
    private void getProfileAndName(final ItemHolder holder, int position) {
        Query query = mFireBaseFireStore.collection(mContext.getResources().getString(R.string.users))
//                .orderBy(mContext.getResources().getString(R.string.userId /*userId*/))
                .whereEqualTo(mContext.getString(R.string.userId), posts.get(position).getUserId());
        Log.d(TAG, "getProfileAndName: ");
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                User user = documentSnapshot.toObject(User.class);
                holder.mUserName.setText(user.getUserDisplayName());
                Glide.with(mContext).load(user.getUserProfile()).into(holder.mProfile);
                Glide.with(mContext).load(user.getUserProfile()).into(holder.mPostImage);
                Log.d(TAG, "onSuccess: "+ user.getUserDisplayName());
            }
        });
    }
}
