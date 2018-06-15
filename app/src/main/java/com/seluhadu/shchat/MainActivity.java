package com.seluhadu.shchat;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.seluhadu.shchat.andapters.ViewPagerAdapter;
import com.seluhadu.shchat.fragments.HomeFragment;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFireBaseAuth;
    private FirebaseFirestore mFireBaseFireStore;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CircleImageView mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFireBaseFireStore = FirebaseFirestore.getInstance();

        mProfile = findViewById(R.id.user_profile);
        mViewPager = findViewById(R.id.main_view_pager);
        mTabLayout = findViewById(R.id.main_tab);
        setUpWitViewPager(mViewPager);
        if (mFireBaseAuth.getCurrentUser() != null) {
            String currentUserId = mFireBaseAuth.getCurrentUser().getUid();
            mFireBaseFireStore.collection("UsersMap").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    assert documentSnapshot != null;
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.get("userName").toString();
                        String userProfile = documentSnapshot.get("userProfile").toString();
                        Uri uri = Uri.parse(userProfile);
                        Glide.with(MainActivity.this).load(uri).into(mProfile);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                mFireBaseAuth.signOut();
                sendToSignIn();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void setUpWitViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new HomeFragment(), "Chat");
        adapter.addFragment(new HomeFragment(), "Friends");
        adapter.addFragment(new HomeFragment(), "Notification");
        adapter.addFragment(new HomeFragment(), "Account");
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
    }
}
