package com.clumob.segment.empty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clumob.segment.controller.SegmentController
import com.clumob.segment.controller.SegmentControllerImpl
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.controller.Storable
import com.clumob.segment.manager.Segment
import com.clumob.segment.manager.SegmentViewHolder
import com.clumob.segment.manager.SegmentViewHolderFactory

/**
 * Created by prashant.rathore on 07/07/18.
 */
class EmptySegment(segmentInfo: SegmentInfo)
    : Segment<Storable?, SegmentController<Storable?>>(segmentInfo, SegmentControllerImpl(null, null)
        , object : SegmentViewHolderFactory {
    override fun create(context: Context?, layoutInflater: LayoutInflater?, parentView: ViewGroup?): SegmentViewHolder<*, *> {
        return object : SegmentViewHolder<Any?, SegmentController<*>?>(context!!, layoutInflater!!, parentView) {
            override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
                return View(context)
            }

            override fun onBind() {}
            override fun onUnBind() {}
        }
    }
})