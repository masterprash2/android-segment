package com.clumob.segment.controller

import com.clumob.listitem.controller.source.ItemUpdatePublisher
import com.clumob.segment.controller.list.SegmentItemControllerImpl

/**
 * Created by prashant.rathore on 03/07/18.
 */
@Deprecated("")
class SegmentPagerItemController(viewModel: SegmentInfo) : SegmentItemControllerImpl(viewModel) {

    override fun onCreate(publisher: ItemUpdatePublisher) {
        super.onCreate(publisher)
    }

    override fun onAttach(source: Any) {
        super.onAttach(source)
    }

    override fun onDetach(source: Any) {
        super.onDetach(source)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override val type: Int
        get() = 0

    override val id: Long
        get() = segmentInfo.id.toLong()

    open val pageTitle: String?
        get() = null
}