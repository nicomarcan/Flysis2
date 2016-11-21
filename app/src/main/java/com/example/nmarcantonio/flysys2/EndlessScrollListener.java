package com.example.nmarcantonio.flysys2;

import android.widget.AbsListView;

/**
 * Created by traies on 20/11/16.
 */

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener{

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private int startingPageIndex = 0;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int currentPage, int startingPageIndex) {
        this.visibleThreshold = visibleThreshold;
        this.currentPage = currentPage;
        this.startingPageIndex = startingPageIndex;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstItem, int visibleCount, int total) {
        if (total < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = total;
            if (total == 0) { this.loading = true;}
        }
        if (loading && total > previousTotalItemCount ) {
            loading = false;
            previousTotalItemCount = total;
            currentPage++;
        }
        if (!loading && (firstItem + visibleCount + visibleThreshold) > total) {
            loading = onLoadMore(currentPage + 1, total);
        }
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);
}
