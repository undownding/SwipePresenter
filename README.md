SwipePresenter
============

A presenter using for RecyclerView &amp; SwipeRefreshLayout

You should only care about `onRefresh()` `onLoadMore()`, and no need to care about `setOnScrolledListener` blablabla.

When 4 items left to show, it will call `onLoadMore()`.

Supports `LinearLayoutManager`, `StaggeredGridLayoutManager`, and `GridLayoutManager`. If you want to support your custom LayoutManager, please implements `SwipePresenter.LayoutManager`.

Download
--------
I'm going to upload to maven but I didn't do it yet : )

Usage
--------

I suggest using this library with lambda:
```
presenter = new SwipePresenter.Builder()
        .onCreated(()-> {
                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL)
                );
                recyclerview.setAdapter(adapter);
            }
        })
        .swipeRefreshLayout(swipeRefreshLayout)
        .recyclerView(recyclerview)
        .emptyView(emptyView)
        .onRefresh(() -> { presenter.stopRefresh(); })
        .onLoadMore(new SwipePresenter.AutoLoadMoreListener(4) { () -> { finishLoadingMore(); } })
        // or you can replace this with presenter.finishLoadingMore();
        .build();
```
Without lambda:
```
presenter = new SwipePresenter.Builder()
        .onCreated(new Runnable() {
            @Override
            public void run() {
                recyclerview.setLayoutManager(new StaggeredGridLayoutManager(
                        2, StaggeredGridLayoutManager.VERTICAL)
                );
                recyclerview.setAdapter(adapter);
            }
        })
        .swipeRefreshLayout(swipeRefreshLayout)
        .recyclerView(recyclerview)
        .emptyView(emptyView)
        .onRefresh(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.stopRefresh();
            }
        })
        .onLoadMore(new SwipePresenter.AutoLoadMoreListener(4) {
            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                finishLoadingMore(); // or you can replace this with presenter.finishLoadingMore();
            }
        })
        .build();
```

License
-------

    Copyright 2016 UnDownDing

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
