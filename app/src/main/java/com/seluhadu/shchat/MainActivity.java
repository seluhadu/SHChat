package com.seluhadu.shchat;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seluhadu.shchat.adapters.ViewPagerAdapter;
import com.seluhadu.shchat.dialogs.DialogNewPost;
import com.seluhadu.shchat.fragments.ChatFragment;
import com.seluhadu.shchat.fragments.HomeFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFireBaseAuth;
    private FirebaseFirestore mFireBaseFireStore;
    private TabLayout mTabLayout;
    private CircleImageView mProfile;
    private ImageButton mMoreVertical;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFireBaseFireStore = FirebaseFirestore.getInstance();
//        more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mFireBaseAuth.signOut();
//            }
//        });
//        mMoreVertical = findViewById(R.id.more_vertical);
//        mMoreVertical.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
//                startActivity(intent);
//            }
//        });
        mProfile = findViewById(R.id.user_profile);
        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mFireBaseAuth.signOut();
//                sendToSignIn();
                DialogNewPost dialogNewPost= new DialogNewPost();
                dialogNewPost.show(getSupportFragmentManager(), dialogNewPost.getTag());
            }
        });
       dialog = new Dialog(this);
       dialog.setTitle("This");
        ViewPager mViewPager = findViewById(R.id.main_view_pager);
        mTabLayout = findViewById(R.id.main_tab);
//        mUserName = findViewById(R.id.user_name);
        setUpWitViewPager(mViewPager);
        if (mFireBaseAuth.getCurrentUser() != null) {
            String currentUserId = mFireBaseAuth.getCurrentUser().getUid();
            DocumentReference dr = mFireBaseFireStore.collection(getResources().getString(R.string.users)).document(currentUserId);
            dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.get("userDisplayName").toString();
                            String userProfile = documentSnapshot.get("userProfile").toString();
                            Uri uri = Uri.parse(userProfile);
//                            mUserName.setText(userName);
                            Glide.with(MainActivity.this).load(uri).into(mProfile);
                        } else {
//                            mUserName.setText(getResources().getString(R.string.unknown));
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFireBaseAuth.getCurrentUser() == null) {
            sendToSignIn();
        }
    }


    private void sendToSignIn() {
        Intent send = new Intent(MainActivity.this, SignInActivity.class);
        send.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(send);
    }

    private void setUpWitViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new ChatFragment(), "Chat");
        adapter.addFragment(new HomeFragment(), "Active");
        adapter.addFragment(new HomeFragment(), "Groups");
        adapter.addFragment(new HomeFragment(), "Calls");
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
    }
}
