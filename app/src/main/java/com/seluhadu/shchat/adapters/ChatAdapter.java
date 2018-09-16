package com.seluhadu.shchat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.models.BaseMessage;
import com.seluhadu.shchat.models.FileMessage;
import com.seluhadu.shchat.models.UserMessage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER_MESSAGE_ME = 10;
    private static final int VIEW_TYPE_USER_MESSAGE_OTHER = 11;

    private static final int VIEW_TYPE_FILE_MESSAGE_ME = 20;
    private static final int VIEW_TYPE_FILE_MESSAGE_OTHER = 21;

    private static final int VIEW_TYPE_FILE_MESSAGE_IMAGE_ME = 22;
    private static final int VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER = 23;

    private static final int VIEW_TYPE_FILE_MESSAGE_VIDEO_ME = 24;
    private static final int VIEW_TYPE_FILE_MESSAGE_VIDEO_OTHER = 25;

    private Context mContext;
    private List<BaseMessage> mMessages;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
        mMessages = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_chat_user, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage baseMessage = mMessages.get(position);
        if (baseMessage instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) baseMessage;
            return userMessage.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ?
                    VIEW_TYPE_USER_MESSAGE_ME : VIEW_TYPE_USER_MESSAGE_OTHER;
        } else if (baseMessage instanceof FileMessage) {
            FileMessage fillMessage = (FileMessage) baseMessage;
            if (fillMessage.getType().startsWith("image")) {
                return fillMessage.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ?
                        VIEW_TYPE_FILE_MESSAGE_IMAGE_ME : VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER;
            } else if (fillMessage.getType().startsWith("video")) {
                return fillMessage.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ?
                        VIEW_TYPE_FILE_MESSAGE_VIDEO_ME : VIEW_TYPE_FILE_MESSAGE_VIDEO_OTHER;
            } else {
                return fillMessage.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ?
                        VIEW_TYPE_FILE_MESSAGE_ME : VIEW_TYPE_FILE_MESSAGE_OTHER;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mMessages != null ? mMessages.size() : 0;
    }

    public void setMessages(List<BaseMessage> Messages) {
        this.mMessages = Messages;
    }

    public void addFirst(BaseMessage baseMessage) {
        mMessages.add(0, baseMessage);
        notifyDataSetChanged();
    }

   public void addLast(BaseMessage message) {
        mMessages.add(message);
        notifyDataSetChanged();
    }

    void delete(long msgId) {
        for (BaseMessage message : mMessages) {
            if (message.getMessageId() == msgId) {
                mMessages.remove(message);
                notifyDataSetChanged();
                break;
            }
        }
    }

    void update(BaseMessage message) {
        BaseMessage baseMessage;
        for (int i = 0; i < mMessages.size(); i++) {
            baseMessage = mMessages.get(i);
            if (message.getMessageId() == baseMessage.getMessageId()) {
                mMessages.remove(i);
                mMessages.add(i, message);
                notifyDataSetChanged();
                break;
            }
        }
    }

    class MyUserMessageHolder extends RecyclerView.ViewHolder {
        private CircleImageView userProfile;
        private TextView userName;
        private TextView userActive;

        MyUserMessageHolder(View itemView) {
            super(itemView);
            this.userActive = itemView.findViewById(R.id.user_active);
            this.userProfile = itemView.findViewById(R.id.user_profile);
            this.userName = itemView.findViewById(R.id.user_name);
        }

        void bind(Context context, BaseMessage baseMessage, int position, boolean isNewDay) {

        }
    }

    class OtherUserMessageHolder extends RecyclerView.ViewHolder {
        private CircleImageView userProfile;
        private TextView userName;
        private TextView userActive;

        OtherUserMessageHolder(View itemView) {
            super(itemView);
            this.userActive = itemView.findViewById(R.id.user_active);
            this.userProfile = itemView.findViewById(R.id.user_profile);
            this.userName = itemView.findViewById(R.id.user_name);
        }

        void bind(Context context, BaseMessage baseMessage, int position, boolean isNewDay) {

        }
    }

    public interface OnItemClickListener {
        void onUserMessageItemClick(UserMessage message, int position);

        void onFileMessageItemClick(FileMessage message, int position);
    }

    public interface OnItemLongClickListener {
        boolean onUserMessageItemLongClick(UserMessage message, int position);

        boolean onFileMessageItemLongClick(FileMessage message, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
