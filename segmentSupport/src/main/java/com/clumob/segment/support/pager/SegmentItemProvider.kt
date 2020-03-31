package com.clumob.segment.support.pager

import com.clumob.segment.controller.common.Controller
import com.clumob.segment.manager.Segment

/**
 * Created by prashant.rathore on 02/07/18.
 */
interface SegmentItemProvider {
    fun provide(segmentItem: Controller): Segment
}