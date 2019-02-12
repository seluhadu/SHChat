package com.seluhadu.shchat.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.adapters.ChatAdapter;
import com.seluhadu.shchat.models.FileMessage;
import com.seluhadu.shchat.models.User;
import com.seluhadu.shchat.models.UserMessage;
import com.seluhadu.shchat.utils.FileUtils;
import com.seluhadu.shchat.utils.FireBaseMethods;

import java.io.File;
import java.util.Hashtable;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private static final int PICK_FILE_REQUEST_CODE = 101;
    private int limit = 5;
    private int lastVis = 0;
    private boolean isPageLoading = false;
    private FirebaseFirestore mFireBaseFireStore;
    private FirebaseAuth mFireBaseAuth;
    private ChatAdapter adapter;
    private RecyclerView mRecyclerView;
    private EditText mEditTextMessage;
    private ImageButton sendMessage;
    private LinearLayoutManager manager;
    private User user;
    private ImageButton pickImage;

    public ChatFragment() {
    }

    public static ChatFragment newInstance(User user) {
        return newInstance(user.getUserId());
    }

    public static ChatFragment newInstance(String receiverId) {
        Bundle args = new Bundle();
        args.putString("receiverId", receiverId);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        mFireBaseAuth = FirebaseAuth.getInstance();
        if (Objects.requireNonNull(getActivity()).getIntent().getExtras() != null) {
            user = getActivity().getIntent().getParcelableExtra("user");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter.loadMsg(user.getUserId(), 100, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_chat, container, false);
        setRetainInstance(true);
        init(rootView);
        textWatcher();

        sendMessage.setEnabled(false);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mEditTextMessage.getText().toString());
            }
        });
        return rootView;
    }

    private void init(View rootView) {
        sendMessage = rootView.findViewById(R.id.send_message);
        pickImage = rootView.findViewById(R.id.more_option);
        mEditTextMessage = rootView.findViewById(R.id.etm);
        mRecyclerView = rootView.findViewById(R.id.chat_recycler_view);
        manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(manager);
        adapter = new ChatAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pick = new Intent(Intent.ACTION_GET_CONTENT);
                pick.setType("image/*");
                startActivityForResult(Intent.createChooser(pick, "Select Picture"), PICK_FILE_REQUEST_CODE);
            }
        });
    }

    private void sendMessage(String msg) {

        UserMessage message = FireBaseMethods.sendUserMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), user.getUserId(), msg, System.currentTimeMillis(), new FireBaseMethods.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage message, String e) {
                if (e != null) {
                    Toast.makeText(getActivity(), "Error: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter.addFirst(message);
        mEditTextMessage.setText("");
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void textWatcher() {
        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != 0) {
                    sendMessage.setEnabled(true);
                    sendMessage.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
                } else {
                    sendMessage.setEnabled(false);
                    sendMessage.getBackground().clearColorFilter();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void sendFileMessage(Uri uri) {
        Hashtable<String, Object> info = FileUtils.getFileInformation(getActivity(), uri);

        if (info == null) {
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        String path = (String) info.get("path");
        File file = new File(path);
        String name = file.getName();
        String mime = (String) info.get("mime");
        int size = (int) info.get("size");
        FileMessage message = FireBaseMethods.sendFileMessage(file, name, mime, size, "", new FireBaseMethods.SendFileMessageHandler() {
            @Override
            public void onSent(FileMessage message, String e) {

            }

            @Override
            public void onProgress(long totalBytesSent, long totalBytesToSend) {
                long progress = (100 * totalBytesSent) / totalBytesToSend;
            }
        });
        adapter.addFirst(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            sendFileMessage(data.getData());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setContext(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}