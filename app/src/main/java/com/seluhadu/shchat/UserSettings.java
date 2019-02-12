package com.seluhadu.shchat;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.seluhadu.shchat.adapters.ViewPagerAdapter;
import com.seluhadu.shchat.fragments.HomeFragment;

public class UserSettings extends AppCompatActivity {
    private ViewPager mViewPager;
    private FirebaseAuth mFireBaseAuth;
    private FirebaseFirestore mFFireStore;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFFireStore = FirebaseFirestore.getInstance();
        mTabLayout = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.posts_view_pager);
        setUpWitViewPager(mViewPager);
    }

    private void setUpWitViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Posts");
        adapter.addFragment(new HomeFragment(), "Friends");
        adapter.addFragment(new HomeFragment(), "Likes");
        mTabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }
}
