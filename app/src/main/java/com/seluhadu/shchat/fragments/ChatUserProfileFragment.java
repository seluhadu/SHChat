package com.seluhadu.shchat.fragments;import android.os.Bundle;import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.fragment.app.Fragment;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import com.seluhadu.shchat.R;import com.seluhadu.shchat.models.User;public class ChatUserProfileFragment extends Fragment {    public ChatUserProfileFragment() {    }    public static ChatUserProfileFragment getInstance(User user){        return newInstance(user.getUserId());    }    public static ChatUserProfileFragment newInstance(String receiverId) {        Bundle args = new Bundle();        args.putString("receiverId", receiverId);        ChatUserProfileFragment fragment = new ChatUserProfileFragment();        fragment.setArguments(args);        return fragment;    }    @Override    public void onCreate(@Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);    }    @Override    public void onActivityCreated(@Nullable Bundle savedInstanceState) {        super.onActivityCreated(savedInstanceState);    }    @Nullable    @Override    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {        View rootView = inflater.inflate(R.layout.fragment_chat_user_profile, container, false);        return rootView;    }}