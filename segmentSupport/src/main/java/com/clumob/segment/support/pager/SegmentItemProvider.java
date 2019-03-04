package com.clumob.segment.support.pager;

import com.clumob.listitem.controller.source.ItemController;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.manager.Segment;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public interface SegmentItemProvider<IC extends ItemController, VM, SC extends SegmentController<VM>> {
    public Segment<VM,SC> provide(IC segmentInfo);
}
