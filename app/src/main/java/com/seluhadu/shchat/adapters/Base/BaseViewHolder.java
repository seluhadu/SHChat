package com.seluhadu.shchat.adapters.Base;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public abstract class  BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, View view);
    }
}
