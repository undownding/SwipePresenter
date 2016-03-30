package me.undownding.swipepresenter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Copyright 2016 SwipePresenter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SwipePresenter {

    private final SwipeRefreshLayout swipeRefreshLayout;
    private final AutoLoadMoreListener onLoadMoreListener;

    private final RecyclerView recyclerView;
    private final View emptyView;

    public SwipePresenter(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, View emptyView,
                                          SwipeRefreshLayout.OnRefreshListener onRefreshListener, AutoLoadMoreListener onLoadMoreListener,
                                          Runnable onCreatedRunnable) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        if (this.swipeRefreshLayout == null || recyclerView == null) {
            throw new NullPointerException("swipeRefreshLayout and swipeRefreshLayout must not be null!!");
        }

        this.recyclerView = recyclerView;
        this.emptyView = emptyView;

        if (onRefreshListener != null) {
            swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        }

        if (onLoadMoreListener != null) {
            recyclerView.addOnScrollListener(onLoadMoreListener);
        }

        this.onLoadMoreListener = onLoadMoreListener;

        onCreatedRunnable.run();
    }

    public final void doRefresh() {
        swipeRefreshLayout.setRefreshing(true);
    }

    public final void finishRefresh() {
        emptyView.setVisibility((recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    public interface LayoutManager {
        int findLastVisibleItemPosition();
    }

    public interface OnLoadMoreListener {

        void onLoadMore(RecyclerView recyclerView);

    }

    public static abstract class AutoLoadMoreListener extends RecyclerView.OnScrollListener implements OnLoadMoreListener {
        private boolean isLoadingMore = false;

        @Override
        public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int lastVisibleItem;
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager){
                lastVisibleItem = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(new int[2])[1] * 2;
            } else if (layoutManager instanceof LayoutManager){
                lastVisibleItem = ((LayoutManager) layoutManager).findLastVisibleItemPosition();
            } else {
                throw new RuntimeException("Can not get last visible item position!!");
            }


            final int totalItemCount = layoutManager.getItemCount();

            if (lastVisibleItem >= totalItemCount - 4 && dy > 0 && !isLoadingMore) {
                this.isLoadingMore = true;
                onLoadMore(recyclerView);
            }
        }

        public final void finishLoadingMore() {
            this.isLoadingMore = false;
        }
    }

    public final void finishLoadingMore() {
        if (this.onLoadMoreListener != null) {
            this.onLoadMoreListener.finishLoadingMore();
        }
    }

    public static final class Builder {
        private SwipeRefreshLayout swipeRefreshLayout;
        private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
        private RecyclerView recyclerView;
        private View emptyView;
        private AutoLoadMoreListener onLoadMoreListener;
        private Runnable onCreatedRunnable;

        public Builder swipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
            return this;
        }

        public Builder recyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        public View getEmptyView() {
            return emptyView;
        }

        public void setEmptyView(View emptyView) {
            this.emptyView = emptyView;
        }

        public Builder onRefresh(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
            this.onRefreshListener = onRefreshListener;
            return this;
        }

        public Builder onLoadMore(AutoLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
            return this;
        }

        public Builder onCreated(Runnable onCreatedRunnable) {
            this.onCreatedRunnable = onCreatedRunnable;
            return this;
        }

        public SwipePresenter build() {
            return new SwipePresenter(swipeRefreshLayout, recyclerView, emptyView, onRefreshListener, onLoadMoreListener, onCreatedRunnable);
        }
    }
}
