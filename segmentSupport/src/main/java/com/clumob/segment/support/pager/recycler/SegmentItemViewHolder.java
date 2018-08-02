package com.clumob.segment.support.pager.recycler;

import com.clumob.recyclerview.adapter.RvViewHolder;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.list.SegmentItemController;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentViewHolder;
import com.clumob.segment.support.pager.SegmentProvider;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public abstract class SegmentItemViewHolder<VM, SC extends SegmentController<VM>> extends RvViewHolder<SegmentItemController> {

    private final SegmentViewHolder<VM, SC> segmentViewHolder;
    private Segment<VM, SC> segment;
    private SegmentInfo<?, ?> segmentInfo;

    private final SegmentProvider<VM,SC> segmentProvider;

    public SegmentItemViewHolder(SegmentViewHolder<VM, SC> viewHolder, SegmentProvider<VM,SC> segmentProvider) {
        super(viewHolder.getView());
        this.segmentViewHolder = viewHolder;
        this.segmentProvider = segmentProvider;
    }

    @Override
    final protected void bindView() {
        destroySegment();
        segment = segmentProvider.provide(getController().getSegmentInfo());
        segment.bindView(segmentViewHolder);
        segment.onStart();
        onBindSegment();
    }

    protected abstract void onBindSegment();


    @Override
    final protected void unBindView() {
        onUnbindSegment();
        destroySegment();
    }

    protected abstract void onUnbindSegment();

    private void destroySegment() {
        if (segment != null) {
            segment.onDestroy();
        }
        segmentInfo = null;
        segment = null;
    }

    public void resume() {
        if (!isResumed()) {
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
