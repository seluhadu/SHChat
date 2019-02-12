package com.seluhadu.shchat.adapters;

import android.content.Context;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.models.BaseMessage;
import com.seluhadu.shchat.models.FileMessage;
import com.seluhadu.shchat.models.UserMessage;
import com.seluhadu.shchat.utils.DateUtil;
import com.seluhadu.shchat.utils.FireBaseMethods;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "ChatAdapter";
    private static final int VIEW_TYPE_USER_MESSAGE_ME = 10;
    private static final int VIEW_TYPE_USER_MESSAGE_OTHER = 11;

    private static final int VIEW_TYPE_FILE_MESSAGE_ME = 20;
    private static final int VIEW_TYPE_FILE_MESSAGE_OTHER = 21;

    private static final int VIEW_TYPE_FILE_MESSAGE_IMAGE_ME = 30;
    private static final int VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER = 31;

    private static final int VIEW_TYPE_FILE_MESSAGE_VIDEO_ME = 40;
    private static final int VIEW_TYPE_FILE_MESSAGE_VIDEO_OTHER = 41;

    static final int ROUND = 0;
    static final int FACED_UP = 1;
    static final int FACED_DOWN = 2;

    @IntDef({ROUND, FACED_UP, FACED_DOWN})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewPosition {
    }

    @ViewPosition
    private int currentPosition = ROUND;


    private Context mContext;
    private List<BaseMessage> mMessages;
    private HashMap<FileMessage, ProgressBar> fileMessageMap;
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
                View userMsgME = null;
                switch (getCurrentPosition()) {
                    case ROUND:
                        userMsgME = LayoutInflater.from(mContext).inflate(R.layout.item_chat_user_me_round, parent, false);
                        break;
                    case FACED_UP:
                        userMsgME = LayoutInflater.from(mContext).inflate(R.layout.item_chat_user_me, parent, false);
                        break;
                    case FACED_DOWN:
                        userMsgME = LayoutInflater.from(mContext).inflate(R.layout.item_chat_user_me, parent, false);
                        break;
                }
                return new MeUserMessageHolder(userMsgME);
            case VIEW_TYPE_USER_MESSAGE_OTHER:
                View userMagOther = LayoutInflater.from(mContext).inflate(R.layout.item_chat_user_other, parent, false);
                return new OtherUserMessageHolder(userMagOther);
            case VIEW_TYPE_FILE_MESSAGE_ME:
                View fileMsgMe = LayoutInflater.from(mContext).inflate(R.layout.item_chat_file_image_me, parent, false);
                return new MeUserMessageHolder(fileMsgMe);
            case VIEW_TYPE_FILE_MESSAGE_OTHER:
                View fileMsgOther = LayoutInflater.from(mContext).inflate(R.layout.item_chat_file_image_other, parent, false);
                return new MeUserMessageHolder(fileMsgOther);
            case VIEW_TYPE_FILE_MESSAGE_IMAGE_ME:
                View fileMsgImgMe = LayoutInflater.from(mContext).inflate(R.layout.item_chat_file_image_me, parent, false);
                return new MeUserMessageHolder(fileMsgImgMe);
            case VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER:
                View fileMsgImgOther = LayoutInflater.from(mContext).inflate(R.layout.item_chat_file_image_other, parent, false);
                return new MeUserMessageHolder(fileMsgImgOther);
            case VIEW_TYPE_FILE_MESSAGE_VIDEO_ME:
                View fileMsgVideoMe = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MeUserMessageHolder(fileMsgVideoMe);
            case VIEW_TYPE_FILE_MESSAGE_VIDEO_OTHER:
                View fileMsgVideoOther = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent, parent, false);
                return new MeUserMessageHolder(fileMsgVideoOther);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BaseMessage message = mMessages.get(position);
        BaseMessage befMsg = mMessages.get(position > 0 ? position - 1 : 0);
        BaseMessage prevMsg = mMessages.get(position < mMessages.size() - 1 ? position + 1 : mMessages.size() - 1);
        isContinuous(befMsg, message, prevMsg);
        int pos1 = position > 0 ? position - 1 : 0;
        int pos2 = position < mMessages.size() - 1 ? position + 1 : mMessages.size() - 1;
        Log.d(TAG, "Position One: " + pos1);
        Log.d(TAG, "Position Two: " + pos2);
        boolean isContinuous = false;
        boolean isNewDay = false;
        if (position < mMessages.size() - 1) {
            BaseMessage previewMsg = mMessages.get(position + 1);
            if (!DateUtil.hasSameDate(message.getCreatedAt(), previewMsg.getCreatedAt())) {
                isNewDay = true;
                isContinuous = false;
            } else {
                isContinuous = isContinuous(message, previewMsg);
            }
        } else if (position == mMessages.size() - 1) {
            isNewDay = true;
        }

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER_MESSAGE_ME:
                ((MeUserMessageHolder) holder).bind(mContext, (UserMessage) message, position, isNewDay, isContinuous);
                break;
            case VIEW_TYPE_USER_MESSAGE_OTHER:
                ((OtherUserMessageHolder) holder).bind(mContext, (UserMessage) message, position, isNewDay, isContinuous);
                break;
            case VIEW_TYPE_FILE_MESSAGE_ME:
                ((MeUserMessageHolder) holder).bind(mContext, (UserMessage) message, position, isNewDay, isContinuous);
                break;
            case VIEW_TYPE_FILE_MESSAGE_OTHER:
                ((MeUserMessageHolder) holder).bind(mContext, (UserMessage) message, position, isNewDay, isContinuous);
                break;
            case VIEW_TYPE_FILE_MESSAGE_IMAGE_ME:
                ((MeImageMessageHolder) holder).bind(mContext, (FileMessage) message, position, isNewDay, isContinuous);
                break;
            case VIEW_TYPE_FILE_MESSAGE_IMAGE_OTHER:
                ((MeUserMessageHolder) holder).bind(mContext, (UserMessage) message, position, isNewDay, isContinuous);
                break;
            case VIEW_TYPE_FILE_MESSAGE_VIDEO_ME:
                ((MeUserMessageHolder) holder).bind(mContext, (UserMessage) message, position, isNewDay, isContinuous);
                break;
            case VIEW_TYPE_FILE_MESSAGE_VIDEO_OTHER:
                ((MeUserMessageHolder) holder).bind(mContext, (UserMessage) message, position, isNewDay, isContinuous);
                break;
            default:
                break;
        }
    }

    private boolean isContinuous(BaseMessage currentMsg, BaseMessage precedingMsg) {
        if (currentMsg == null || precedingMsg == null) return false;

        String currentUser = null, precedingUser = null;
        if (currentMsg instanceof UserMessage) {
            currentUser = (currentMsg).getUserId();
        } else if (currentMsg instanceof FileMessage) {
            currentUser = (currentMsg).getUserId();
        }
        if (precedingMsg instanceof UserMessage) {
            precedingUser = (precedingMsg).getUserId();
        } else if (precedingMsg instanceof FileMessage) {
            precedingUser = (precedingMsg).getUserId();
        }
        return ((currentUser != null && precedingUser != null) && currentUser.equals(precedingUser));
    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = mMessages.get(position);
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            return userMessage.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) ?
                    VIEW_TYPE_USER_MESSAGE_ME : VIEW_TYPE_USER_MESSAGE_OTHER;
        } else if (message instanceof FileMessage) {
            FileMessage fillMessage = (FileMessage) message;
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

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setMessages(List<BaseMessage> Messages) {
        this.mMessages = Messages;
        notifyDataSetChanged();
    }

    public void addFirst(BaseMessage baseMessage) {
        mMessages.add(0, baseMessage);
        notifyDataSetChanged();
    }

    public void addLast(BaseMessage message) {
        mMessages.add(message);
        notifyDataSetChanged();
    }

    private void setCurrentPosition(@ViewPosition int currentPosition) {
        this.currentPosition = currentPosition;
    }

    @ViewPosition
    private int getCurrentPosition() {
        return currentPosition;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isNewDay() {
        if (mMessages.size() != 0)
            return DateUtil.hasSameDate(mMessages.get(0).getCreatedAt(), System.currentTimeMillis());
        else return false;
    }

    void delete(final RecyclerView recyclerView, final int position, long msgId) {
        for (final BaseMessage message : mMessages) {
            if (message.getMessageId() == msgId) {
                Snackbar snackbar = Snackbar.make(recyclerView, "Message Deleted!", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMessages.add(position, message);
                                notifyItemChanged(position);
                                recyclerView.scrollToPosition(position);
                            }
                        });
                snackbar.show();
                mMessages.remove(message);
                notifyItemRemoved(position);
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


    public void isContinuous(BaseMessage before, BaseMessage current, BaseMessage after) {
        if (before == null || current == null || after == null) return;
        if (current.getUserId().equals(before.getUserId()) && current.getUserId().equals(after.getUserId()) ||
                !current.getUserId().equals(before.getUserId()) && !current.getUserId().equals(after.getUserId())) {
            // current == circular
            setCurrentPosition(ROUND);
        } else if (current.getUserId().equals(before.getUserId())) {
            // current faced up
            setCurrentPosition(FACED_UP);
        } else if (current.getUserId().equals(after.getUserId())) {
            // current faced down
            setCurrentPosition(FACED_DOWN);
        }
    }

    public void isContinuousl(BaseMessage before, BaseMessage current, BaseMessage after) {
        if (before == null || current == null || after == null) return;
        if (current.getUserId().equals(before.getUserId()) && current.getUserId().equals(after.getUserId()) ||
                !current.getUserId().equals(before.getUserId()) && !current.getUserId().equals(after.getUserId())) {
            // curent == circular
            setCurrentPosition(ROUND);
        } else if (current.getUserId().equals(before.getUserId())) {
            // current faced up
            setCurrentPosition(FACED_UP);
        } else if (current.getUserId().equals(after.getUserId())) {
            // current faced down
            setCurrentPosition(FACED_DOWN);
        }
    }

    public void markMessageSent(BaseMessage message) {
        BaseMessage msg;
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            msg = mMessages.get(i);
            if (message instanceof UserMessage && msg instanceof UserMessage) {

            }
        }
    }

    public void serFileMessagePercent(FileMessage message, int perecent) {
        BaseMessage msg;
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            msg = mMessages.get(i);
            if (msg instanceof FileMessage) {

            }
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

    public void loadMsg(final String receiverId, int limit, final FireBaseMethods.GetMessagesHandler handler) {
        FireBaseMethods.getMessagesByTimestamp(FireBaseMethods.sortedUsersId(FirebaseAuth.getInstance().getCurrentUser().getUid(), receiverId), limit, new FireBaseMethods.GetMessagesHandler() {
            @Override
            public void onResult(List<BaseMessage> messageList, Exception e) {
                if (handler != null) {
                    handler.onResult(messageList, e);
                }
                if (e != null) {
                    e.printStackTrace();
                    return;
                }

                for (BaseMessage message : messageList) {
                    mMessages.add(message);
                }
                notifyDataSetChanged();
            }
        });
    }

    class MeUserMessageHolder extends RecyclerView.ViewHolder {
        private ImageView readCheck;
        private TextView mMessage;
        private TextView timeStamp;
        private TextView newDay;
        private LinearLayout dateContainer;

        MeUserMessageHolder(View itemView) {
            super(itemView);
            this.readCheck = itemView.findViewById(R.id.checkRead);
            this.mMessage = itemView.findViewById(R.id.message);
            this.newDay = itemView.findViewById(R.id.new_day_time);
            this.timeStamp = itemView.findViewById(R.id.time_stamp);
            this.dateContainer = itemView.findViewById(R.id.dateContainer);
        }

        void bind(Context context, UserMessage userMessage, int position, boolean isNewDay, boolean isContunuios) {
            mMessage.setText(userMessage.getMessage());
            timeStamp.setText(DateUtil.formatTimeWithMarker(userMessage.getCreatedAt()));
            if (isNewDay) {
                dateContainer.setVisibility(View.VISIBLE);
                newDay.setText(DateUtil.formatDateTime(userMessage.getNewDate()));
                Log.d(TAG, "newDate: " + userMessage.getNewDate());
            } else {
                dateContainer.setVisibility(View.GONE);
            }
        }
    }

    class OtherUserMessageHolder extends RecyclerView.ViewHolder {
        private ImageView mUserProfile;
        private TextView mMessage;
        private TextView userActive;
        private TextView timeStamp;
        private TextView mDateUploaded;
        private LinearLayout dateContainer;

        OtherUserMessageHolder(View itemView) {
            super(itemView);
//            this.userActive = itemView.findViewById(R.id.user_active);
            this.mUserProfile = itemView.findViewById(R.id.user_profile);
            this.mMessage = itemView.findViewById(R.id.message);
            this.mDateUploaded = itemView.findViewById(R.id.new_day_time);
            this.timeStamp = itemView.findViewById(R.id.time_stamp);
            this.dateContainer = itemView.findViewById(R.id.dateContainer);
        }

        void bind(Context context, UserMessage message, int position, boolean isNewDay, boolean isContunuios) {
            mMessage.setText(message.getMessage());
            Glide.with(mContext).load(message.getMessage()).into(mUserProfile);
            if (message.getUpdatedAt() > 0) {
                timeStamp.setText(DateUtil.formatTimeWithMarker(message.getUpdatedAt()));
            } else {
                timeStamp.setText(DateUtil.formatTimeWithMarker(message.getCreatedAt()));
            }
            if (isNewDay) {
                dateContainer.setVisibility(View.VISIBLE);
                mDateUploaded.setText(DateUtil.formatDateTime(message.getNewDate()));
            } else {
                dateContainer.setVisibility(View.GONE);
            }
        }
    }

    class MeImageMessageHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView dateUploded;
        private TextView userActive;

        MeImageMessageHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.image);
            this.dateUploded = itemView.findViewById(R.id.date_posted);
        }

        void bind(Context context, FileMessage message, int position, boolean isNewDay, boolean isContinous) {
            Glide.with(context).load(message.getUrl()).into(this.imageView);
        }
    }

    class OtherFileMessageHolder extends RecyclerView.ViewHolder {
        private CircleImageView userProfile;
        private TextView mMessage;
        private TextView userName;
        private TextView userActive;

        OtherFileMessageHolder(View itemView) {
            super(itemView);
            this.userActive = itemView.findViewById(R.id.user_active);
            this.userProfile = itemView.findViewById(R.id.user_profile);
            this.userName = itemView.findViewById(R.id.user_name);
        }

        void bind(Context context, BaseMessage baseMessage, int position, boolean isNewDay) {
        }
    }
}
