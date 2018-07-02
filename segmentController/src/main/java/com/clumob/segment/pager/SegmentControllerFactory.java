package com.clumob.segment.pager;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.presenter.SegmentInfo;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public interface SegmentControllerFactory {
    public SegmentController<?> create(SegmentInfo segmentInfo);
}
