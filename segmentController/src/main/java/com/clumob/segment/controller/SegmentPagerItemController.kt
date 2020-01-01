package com.clumob.segment.controller

import com.clumob.listitem.controller.source.ItemControllerImpl
import com.clumob.listitem.controller.source.ItemUpdatePublisher

/**
 * Created by prashant.rathore on 03/07/18.
 */
@Deprecated("")
class SegmentPagerItemController(viewModel: SegmentInfo) : ItemControllerImpl<SegmentInfo?>(viewModel) {

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

    override fun getType(): Int {
        return 0
    }

    override fun getId(): Long {
        return viewData!!.id.toLong()
    }

    val pageTitle: String?
        get() = null
}