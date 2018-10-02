package com.seluhadu.shchat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.adapters.ChatAdapter;
import com.seluhadu.shchat.chat_interactore.ChatContractor;
import com.seluhadu.shchat.chat_interactore.ChatPresenter;
import com.seluhadu.shchat.models.BaseMessage;
import com.seluhadu.shchat.models.UserMessage;

public class ChatFragment extends Fragment implements ChatContractor.view {
    private static final String TAG = "ChatFragment";

    private FirebaseFirestore mFireBaseFireStore;
    private FirebaseAuth mFireBaseAuth;
    private ChatAdapter adapter;
    private RecyclerView mRecyclerView;
    private ChatPresenter chatPresenter;
    private EditText mEditTextMessage;
    ListenerRegistration mListenerRegistration;
    private ImageButton sendMessage;

    public ChatFragment() {
    }

    public static ChatFragment newInstance(String senderId, String receiverId, String fireBaseToken) {
        Bundle args = new Bundle();
        args.putString("senderId", senderId);
        args.putString("receiverId", receiverId);
        args.putString("fireBaseToken", fireBaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        mFireBaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = rootView.findViewById(R.id.chat_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ChatAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        sendMessage = rootView.findViewById(R.id.send_message);
        mEditTextMessage = rootView.findViewById(R.id.etm);
        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != 0) {
                    sendMessage.setEnabled(true);
                } else sendMessage.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendMessage.setEnabled(false);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        chatPresenter = new ChatPresenter(this);
        chatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString("receiverId"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mListenerRegistration != null) mListenerRegistration.remove();
    }

    @Override
    public void onSendMessageSuccess() {

    }

    @Override
    public void onSendMessageFailure(String error) {

    }

    @Override
    public void onGetMessageSuccess(BaseMessage message) {
        adapter.addLast(message);
    }

    @Override
    public void onGetMessageFailure(String error) {

    }

    private void sendMessage() {
        String message = mEditTextMessage.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            UserMessage userMessage = new UserMessage();
            userMessage.setMessage(message);
            userMessage.setCreatedAt(System.currentTimeMillis());
            userMessage.setMessageId(System.currentTimeMillis());
            userMessage.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userMessage.setReceiverId(getArguments().getString("receiverId"));
            userMessage.setMsgType("UMSG");
            mEditTextMessage.setText("");
            adapter.addLast(userMessage);
            chatPresenter.sendMessage(getActivity(), userMessage, getArguments().getString("receiverId"));
        }
    }
}