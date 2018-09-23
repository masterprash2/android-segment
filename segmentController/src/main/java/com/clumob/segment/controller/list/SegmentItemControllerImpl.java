package com.clumob.segment.controller.list;

import com.clumob.listitem.controller.source.ItemController;
import com.clumob.listitem.controller.source.ItemUpdatePublisher;
import com.clumob.segment.controller.SegmentInfo;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public abstract class SegmentItemControllerImpl implements SegmentItemController {

    private final SegmentInfo segmentInfo;

    public SegmentItemControllerImpl(SegmentInfo segmentInfo) {
        this.segmentInfo = segmentInfo;
    }


    @Override
    final public void onCreate(ItemUpdatePublisher itemUpdatePublisher) {

    }

    @Override
    final public void onAttach() {

    }

    @Override
    final public void onDetach() {

    }

    @Override
    final public void onDestroy() {

    }

    public final SegmentInfo getSegmentInfo() {
        return segmentInfo;
    }

}
