package com.clumob.segment.support.pager;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.controller.SegmentInfo;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public interface SegmentProvider<VM, SC extends SegmentController<VM>> {
    public Segment<VM,SC> provide(SegmentInfo segmentInfo);
}
