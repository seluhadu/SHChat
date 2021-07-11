package com.seluhadu.shchat.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private RecyclerView.LayoutManager mLayoutManager;
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean isLoading = true;
    private int startingPageIndex = 0;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public PaginationScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        this.visibleThreshold = this.visibleThreshold * layoutManager.getSpanCount();
    }

    public PaginationScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0 || lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy > 0) {
            OnScrollDown();
            if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                OnEndScroll();
            }
        } else if (dy < 0) {
            OnScrollUp();
        }
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.isLoading = true;
            }
        }
        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!isLoading && (lastVisibleItemPosition + visibleThreshold) >= totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            isLoading = true;
        }
    }

    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.isLoading = true;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    public abstract void OnScrollDown();

    public abstract void OnScrollUp();

    public abstract void OnEndScroll();
}
