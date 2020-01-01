package com.clumob.segment.controller.list

import com.clumob.listitem.controller.source.ItemController
import com.clumob.segment.controller.SegmentInfo

/**
 * Created by prashant.rathore on 11/07/18.
 */
interface SegmentItemController : ItemController {
    val segmentInfo: SegmentInfo?
}