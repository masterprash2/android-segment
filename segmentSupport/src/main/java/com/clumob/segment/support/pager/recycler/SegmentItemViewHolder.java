package com.clumob.segment.support.pager.recycler;

import android.view.View;

import com.clumob.recyclerview.adapter.RvViewHolder;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.list.SegmentItemController;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentViewHolder;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public abstract class SegmentItemViewHolder<VM, SC extends SegmentController<VM>> extends RvViewHolder<SegmentItemController> {

    private final SegmentViewHolder<VM, SC> segmentViewHolder;
    private Segment<VM, SC> segment;
    private SegmentInfo<?, ?> segmentInfo;
    private boolean isAttached;

    public SegmentItemViewHolder(View view, SegmentViewHolder<VM, SC> viewHolder) {
        super(view);
        this.segmentViewHolder = viewHolder;
    }

    @Override
    final protected void bindView() {
        if (segment == null || segmentInfo != getController().getSegmentInfo()) {
            destroySegment();
            this.segmentInfo = getController().getSegmentInfo();
            segment = createSegment(segmentInfo);
            segment.bindView(segmentViewHolder);
        }
        onBindSegment();
    }

    protected abstract Segment createSegment(SegmentInfo<?, ?> segmentInfo);

    @Override
    protected void onAttached() {
        this.isAttached = true;
        super.onAttached();
        segment.onStart();
        resume();

    }

    protected abstract void onBindSegment();

    @Override
    final protected void unBindView() {
        onUnbindSegment();
        destroySegment();
    }

    protected abstract void onUnbindSegment();

    @Override
    public void onDetached() {
        this.isAttached = false;
        pause();
        segment.onStop();
        super.onDetached();
    }

    private void destroySegment() {
        if (segment != null) {
            segment.onDestroy();
        }
        segmentInfo = null;
        segment = null;
    }

    @Override
    protected void onScreenIsInFocus() {
        super.onScreenIsInFocus();
        resume();
    }

    @Override
    protected void onScreenIsOutOfFocus() {
        pause();
        super.onScreenIsOutOfFocus();
    }

    public void resume() {
        if (!isResumed() && isAttached) {
            segment.onResume();
        }
    }

    public Segment<VM, SC> getSegment() {
        return segment;
    }

    public boolean isResumed() {
        return segment != null && segment.isResumed();
    }

    public void pause() {
        if (isResumed()) {
            segment.onPause();
        }
    }

    public boolean handleBackPressed() {
        return segmentViewHolder.handleBackPressed();
    }
}
