package com.seluhadu.shchat;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seluhadu.shchat.adapters.ViewPagerAdapter;
import com.seluhadu.shchat.fragments.HomeFragment;
import com.seluhadu.shchat.fragments.ListChatFragment;
import com.seluhadu.shchat.multi_edittext.EditTextActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFireBaseAuth;
    private FirebaseFirestore mFireBaseFireStore;
    private TabLayout mTabLayout;
    private CircleImageView mProfile;
    private ImageButton mMoreVertical;
    private Dialog dialog;
    private ViewPagerAdapter pagerAdapter;

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
        mProfile.setOnClickListener(v -> {
//            mFireBaseAuth.signOut();
//            sendToSignIn();
                Intent intent = new Intent(MainActivity.this, EditTextActivity.class);
                startActivity(intent);
//                DialogNewPost dialogNewPost= new DialogNewPost();
//                dialogNewPost.show(getSupportFragmentManager(), dialogNewPost.getTag());
        });
        dialog = new Dialog(this);
        dialog.setTitle("This");
        ViewPager mViewPager = findViewById(R.id.main_view_pager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mTabLayout = findViewById(R.id.main_tab);
//        mUserName = findViewById(R.id.user_name);

        if (mFireBaseAuth.getCurrentUser() != null) {
            setUpWitViewPager(mViewPager);
            String currentUserId = mFireBaseAuth.getCurrentUser().getUid();
            DocumentReference dr = mFireBaseFireStore.collection(getResources().getString(R.string.users)).document(currentUserId);
            dr.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.get("userDisplayName").toString();
                        String userProfile = documentSnapshot.get("userProfile").toString();
                        Uri uri = Uri.parse(userProfile);
//                            mUserName.setText(userName);
                        Glide.with(getApplicationContext()).load(uri).into(mProfile);
                    } else {
//                            mUserName.setText(getResources().getString(R.string.unknown));
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
        pagerAdapter.addFragment(new HomeFragment(), "Home");
        pagerAdapter.addFragment(new ListChatFragment(), "Chat");
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        viewPager.setAdapter(pagerAdapter);
    }
}
