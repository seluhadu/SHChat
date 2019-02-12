package com.seluhadu.shchat.adapters.Base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, Model> extends RecyclerView.Adapter<VH> {
    protected ArrayList<Model> mData;
    private Context mContext;

    public BaseAdapter(ArrayList<Model> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public ArrayList<Model> getData() {
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

    protected abstract int getLayoutId(int viewType);

    protected abstract VH getViewHolder(View itemView, int viewType);
}
