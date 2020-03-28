package com.clumob.segment.controller.list

import com.clumob.listitem.controller.source.ItemUpdatePublisher
import com.clumob.segment.controller.SegmentInfo

/**
 * Created by prashant.rathore on 11/07/18.
 */
abstract class SegmentItemControllerImpl(val segmentInfo: SegmentInfo) : SegmentItemController {

    override fun segmentInfo(): SegmentInfo {
        return segmentInfo
    }

    override fun onCreate(itemUpdatePublisher: ItemUpdatePublisher) {}
    override fun onAttach(source: Any) {}
    override fun onDetach(source: Any) {}
    override fun onDestroy() {}

}