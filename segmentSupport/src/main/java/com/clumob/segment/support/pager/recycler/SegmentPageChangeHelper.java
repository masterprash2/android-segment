package com.clumob.segment.support.pager.recycler;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public class SegmentPageChangeHelper {

    private final SnapHelper snapHelper;
    private SegmentItemViewHolder previousFocusedView;
    private int currentPageIndex = -1;
    private final PageChangeListner pageChangeListner;
    private final List<PageChangeListner> pageChangeListners = new LinkedList<>();

    private RecyclerView attachedRecyclerView;

    public SegmentPageChangeHelper(SnapHelper snapHelper) {
        this.snapHelper = snapHelper;
        this.pageChangeListner = createPageChangeListener();
    }

    private PageChangeListner createPageChangeListener() {
        return new PageChangeListner() {
            @Override
            public void onPageChanged(int position) {
                for (PageChangeListner listner : pageChangeListners) {
                    listner.onPageChanged(position);
                }
            }

            @Override
            public void onPageScrolled(int position, float offset) {
                for (PageChangeListner listner : pageChangeListners) {
                    listner.onPageScrolled(position, offset);
                }
            }
        };
    }

    public void addPageChangeListner(PageChangeListner listner) {
        this.pageChangeListners.add(listner);
    }

    RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (previousFocusedView == null)
                return;
            float x;
            int adapterPosition = previousFocusedView.getAdapterPosition();
            SegmentItemViewHolder snapView = previousFocusedView;
            View itemView = snapView.getItemView();
            x = itemView.getLeft();
            if (snapView.getItemView().getLeft() <= 0) {
                pageChangeListner.onPageScrolled(adapterPosition, -x * 1f / itemView.getMeasuredWidth());
            } else {
                x = x - itemView.getMeasuredWidth();
                pageChangeListner.onPageScrolled(adapterPosition - 1, -x * 1f / itemView.getMeasuredWidth());
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                SegmentItemViewHolder snapView = (SegmentItemViewHolder) recyclerView.getChildViewHolder(view);
                int pageIndex = snapView.getAdapterPosition();
                if (pageIndex != currentPageIndex) {
                    if (previousFocusedView != null) {
                        previousFocusedView.pause();
                    }
                    snapView.resume();
                    currentPageIndex = snapView.getAdapterPosition();
                    previousFocusedView = snapView;
                    pageChangeListner.onPageChanged(currentPageIndex);
                }
            }
        }
    };

    public void attachRecyclerView(final RecyclerView recyclerView) {
        this.attachedRecyclerView = recyclerView;
        recyclerView.addOnScrollListener(listener);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                listener.onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
            }
        });
    }

    public void scrollToPosition(int position) {
        if (currentPageIndex != position) {
            attachedRecyclerView.scrollToPosition(position);
        }
    }

    public static interface PageChangeListner {
        public void onPageChanged(int position);

        public void onPageScrolled(int position, float offset);
    }
}
