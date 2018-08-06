package com.seluhadu.shchat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seluhadu.shchat.R;
import com.seluhadu.shchat.models.BaseMessage;
import com.seluhadu.shchat.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemHolder> {
    private static final int VIEW_TYPE_USER_MESSAGE = 10;
    private static final int VIEW_TYPE_FILE_MESSAGE = 20;

    private Context mContext;
    private ArrayList<BaseMessage> mMessage;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public ChatAdapter(Context mContext, ArrayList<BaseMessage> Message) {
        this.mContext = mContext;
        this.mMessage = Message;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_user, parent, false);
        return new ItemHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
//        User user = users.get(position);
//        holder.userName.setText(user.getUserDisplayName());
//        holder.userActive.setText(user.getUserDisplayName());
//        Glide.with(mContext).load(user.getUserProfile()).into(holder.userProfile);
    }

    @Override
    public int getItemCount() {
        return mMessage != null ? mMessage.size() : 0;
    }

    void addFirst(BaseMessage baseMessage) {
        mMessage.add(0, baseMessage);
        notifyDataSetChanged();
    }

    void addLast(BaseMessage message) {
        mMessage.add(message);
        notifyDataSetChanged();
    }

    void delete(long msgId) {
        for (BaseMessage message : mMessage) {
            if (message.getmMessageId() == msgId) {
                mMessage.remove(message);
                notifyDataSetChanged();
                break;
            }
        }
    }

    void update(BaseMessage message) {
        BaseMessage baseMessage;
        for (int i = 0; i < mMessage.size(); i++) {
            baseMessage = mMessage.get(i);
            if (message.getmMessageId() == baseMessage.getmMessageId()) {
                mMessage.remove(i);
                mMessage.add(i, message);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void setUsers(ArrayList<User> message) {
        this.mMessage = mMessage;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private CircleImageView userProfile;
        private TextView userName;
        private TextView userActive;

        ItemHolder(View itemView) {
            super(itemView);
            this.userActive = itemView.findViewById(R.id.user_active);
            this.userProfile = itemView.findViewById(R.id.user_profile);
            this.userName = itemView.findViewById(R.id.user_name);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                onItemClickListener.OnItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return ((onItemLongClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) && onItemLongClickListener.OnItemLongClick(v, getAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean OnItemLongClick(View view, int position);
    }
}
