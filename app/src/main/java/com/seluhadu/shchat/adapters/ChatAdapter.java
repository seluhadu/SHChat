package com.seluhadu.shchat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.models.BaseMessage;
import com.seluhadu.shchat.models.FileMessage;
import com.seluhadu.shchat.models.UserMessage;
import com.seluhadu.shchat.utils.DateUtil;

import java.io.File;
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
        switch (viewType) {
            case VIEW_TYPE_USER_MESSAGE_ME:
                View userMsgME = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(userMsgME);
            case VIEW_TYPE_USER_MESSAGE_OTHER:
                View userMagOther = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(userMagOther);

            case VIEW_TYPE_FILE_MESSAGE_ME:
                View fileMsgMe = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(fileMsgMe);
            case VIEW_TYPE_FILE_MESSAGE_OTHER:
                View fileMsgOther = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(fileMsgOther);

            case VIEW_TYPE_FILE_MESSAGE_IMAGE_ME:
                View fileMsgImgMe = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(fileMsgImgMe);
            case VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER:
                View fileMsgImgOther = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(fileMsgImgOther);

            case VIEW_TYPE_FILE_MESSAGE_VIDEO_ME:
                View fileMsgVideoMe = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(fileMsgVideoMe);
            case VIEW_TYPE_FILE_MESSAGE_VIDEO_OTHER:
                View fileMsgVideoOther = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MyUserMessageHolder(fileMsgVideoOther);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseMessage baseMessage = mMessages.get(position);
        boolean isContinuous = false;
        boolean isNewDay = false;
        if (position == mMessages.size() - 1) {
            BaseMessage prevMsg = mMessages.get(position + 1);
            if (!DateUtil.hasSameDate(baseMessage.getCreatedAt(), prevMsg.getCreatedAt())) {
                isNewDay = true;
                isContinuous = false;
            } else {
                isContinuous = isContinuous(baseMessage, prevMsg);
            }
        }

        switch (getItemViewType(position)) {
            case VIEW_TYPE_USER_MESSAGE_ME:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);
            case VIEW_TYPE_USER_MESSAGE_OTHER:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);

            case VIEW_TYPE_FILE_MESSAGE_ME:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);
            case VIEW_TYPE_FILE_MESSAGE_OTHER:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);

            case VIEW_TYPE_FILE_MESSAGE_IMAGE_ME:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);
            case VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);

            case VIEW_TYPE_FILE_MESSAGE_VIDEO_ME:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);
            case VIEW_TYPE_FILE_MESSAGE_VIDEO_OTHER:
                ((MyUserMessageHolder) holder).bind(mContext, (UserMessage) baseMessage, position, isNewDay);
        }
    }

    private boolean isContinuous(BaseMessage currentMsg, BaseMessage precedingMsg) {
        if (currentMsg == null || precedingMsg == null) return false;

        String currentUser = null, precedingUser = null;
        if (currentMsg instanceof UserMessage) {
            currentUser = ((UserMessage) currentMsg).getUserId();
        } else if (currentMsg instanceof FileMessage) {
            currentUser = ((FileMessage) currentMsg).getUserId();
        }

        if (precedingMsg instanceof UserMessage) {
            precedingUser = ((UserMessage) precedingMsg).getUserId();
        } else if (precedingMsg instanceof FileMessage) {
            precedingUser = ((FileMessage) precedingMsg).getUserId();
        }
        return !(currentUser == null || precedingUser == null) & currentUser.equals(precedingUser);
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
        private ImageView userProfile;
        private TextView mMessage;
        private TextView userName;
        private TextView userActive;
        View view = itemView;

        MyUserMessageHolder(View itemView) {
            super(itemView);
            this.userActive = itemView.findViewById(R.id.user_active);
            this.userProfile = itemView.findViewById(R.id.user_profile);
            this.userName = itemView.findViewById(R.id.user_name);
            this.mMessage = itemView.findViewById(R.id.message);
        }

        void bind(Context context, UserMessage userMessage, int position, boolean isNewDay) {
            mMessage.setText(userMessage.getMessage());
        }
    }

    class OtherUserMessageHolder extends RecyclerView.ViewHolder {
        private CircleImageView userProfile;
        private TextView mMessage;
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
