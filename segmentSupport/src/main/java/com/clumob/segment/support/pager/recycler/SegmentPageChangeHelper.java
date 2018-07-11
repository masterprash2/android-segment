package com.clumob.segment.support.pager.recycler;

import android.support.v7.widget.RecyclerView;

import com.clumob.segment.controller.list.SegmentItemController;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public class SegmentPageChangeHelper  {

    private final SnapViewFinder<? extends SegmentItemController,? extends SegmentItemViewHolder> segmentItemViewFinder;
    private SegmentItemViewHolder previousFocusedView;

    public SegmentPageChangeHelper(SnapViewFinder<? extends  SegmentItemController,? extends SegmentItemViewHolder> segmentItemViewFinder) {
        this.segmentItemViewFinder = segmentItemViewFinder;
    }

    RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (previousFocusedView != null) {
                    previousFocusedView.pause();
                }
                SegmentItemViewHolder snapView = segmentItemViewFinder.findSnapView(recyclerView.getLayoutManager());
                snapView.resume();
                previousFocusedView = snapView;
            }
        }
    };

    public void attachRecyclerView(final RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(listener);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                listener.onScrollStateChanged(recyclerView,RecyclerView.SCROLL_STATE_IDLE);
            }
        });
    }
}
