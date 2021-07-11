package com.seluhadu.shchat.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.seluhadu.shchat.R;
import com.seluhadu.shchat.adapters.HomeAdapter;
import com.seluhadu.shchat.models.Photo;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseAuth mFireBaseAuth;
    private FirebaseFirestore mFireBaseFireStore;
    private StorageReference mStorageReference;
    private RecyclerView mRecyclerView;
    private RecyclerView mFeedRecyclerView;
    private ArrayList<Photo> mPhotos;
    private HomeAdapter recyclerAdapter;
    private ProgressBar mProgressBar;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mFireBaseFireStore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        mPhotos = new ArrayList<>();
        inti(rootView);
        getData();
        return rootView;
    }

    private void inti(View view) {
        mRecyclerView = view.findViewById(R.id.home_recycler_view);
        mFeedRecyclerView = view.findViewById(R.id.feed_recycler_view);
        mFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mProgressBar = view.findViewById(R.id.progress_bar);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter = new HomeAdapter(getActivity(), mPhotos);
        mRecyclerView.setAdapter(recyclerAdapter);
    }

    private void getData() {
        mFireBaseFireStore.collection(getResources().getString(R.string.posts))/*.orderBy("datePosted", Query.Direction.ASCENDING)*/
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            Photo photo = documentChange.getDocument().toObject(Photo.class);
                            mPhotos.add(photo);
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }
                    hideProgressBar();
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerAdapter.setContext(getActivity());
    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
