package com.clumob.segment.support.pager

import com.clumob.segment.controller.SegmentController
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.manager.Segment

/**
 * Created by prashant.rathore on 02/07/18.
 */
interface SegmentProvider< SC : SegmentController?> {
    fun provide(segmentInfo: SegmentInfo?): Segment<SC>?
}