package com.seluhadu.shchat.adapters.Base;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, Model> extends RecyclerView.Adapter<VH> {
    private ArrayList<Model> mData;
    private Context mContext;

    public BaseAdapter(ArrayList<Model> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    protected ArrayList<Model> getData() {
        return mData;
    }

    public void setData(ArrayList<Model> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        return getViewHolder(rootView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    abstract int getLayoutId(int viewType);

    abstract VH getViewHolder(View itemView, int viewType);
}
