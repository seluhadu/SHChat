package com.seluhadu.shchat.adapters;import android.content.Context;import android.support.annotation.NonNull;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import com.bumptech.glide.Glide;import com.seluhadu.shchat.R;import com.seluhadu.shchat.models.User;import java.util.List;import de.hdodenhof.circleimageview.CircleImageView;public class ListUsersAdapter extends RecyclerView.Adapter<ListUsersAdapter.ItemHolder> {    private List<User> mListUserChat;    private Context mContext;    private OnItemClickListener onItemClickListener;    public ListUsersAdapter(List<User> mListUserChat, Context mContext) {        this.mListUserChat = mListUserChat;        this.mContext = mContext;    }    @NonNull    @Override    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_list_chat_user, parent, false);        return new ItemHolder(rootView);    }    @Override    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {        User user = mListUserChat.get(position);        holder.mUserName.setText(user.getUserName());        Glide.with(mContext).load(user.getUserProfile()).into(holder.iUserProfile);    }    @Override    public int getItemCount() {        return mListUserChat == null ? 0 : mListUserChat.size();    }    class ItemHolder extends RecyclerView.ViewHolder {        CircleImageView iUserProfile;        TextView mUserName;        TextView mMessage;        TextView mDate;        ItemHolder(View itemView) {            super(itemView);            mUserName = itemView.findViewById(R.id.user_name);            mMessage = itemView.findViewById(R.id.message_time);            mDate = itemView.findViewById(R.id.last_message);            iUserProfile = itemView.findViewById(R.id.user_profile);            itemView.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    if (onItemClickListener!=null){                        onItemClickListener.OnItemClick(mListUserChat.get(getAdapterPosition()), getAdapterPosition());                    }                }            });        }    }    public interface  OnItemClickListener{        void OnItemClick(User user, int position);    }    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {        this.onItemClickListener = onItemClickListener;    }}