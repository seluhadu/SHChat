package com.seluhadu.shchat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.adapters.ChatAdapter;
import com.seluhadu.shchat.models.BaseMessage;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";

    private FirebaseFirestore mFireBaseFireStore;
    private FirebaseAuth mFireBaseAuth;
    private ChatAdapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<BaseMessage> users;
    ListenerRegistration mListenerRegistration;

    public ChatFragment() {
    }

    public static ChatFragment newInstance() {

        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = rootView.findViewById(R.id.chat_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        adapter = new ChatAdapter(getActivity(), users);
        mRecyclerView.setAdapter(adapter);
        mFireBaseAuth= FirebaseAuth.getInstance();
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        String userId = mFireBaseAuth.getCurrentUser().getUid();
        Query query = mFireBaseFireStore.collection(getString(R.string.users))
                .document(userId).collection("Chats");
        mListenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "onEvent: error", e);
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {

                }
            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mListenerRegistration.remove();
    }
}