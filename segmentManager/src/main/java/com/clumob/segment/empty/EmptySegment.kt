package com.clumob.segment.empty

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clumob.segment.controller.common.BasicController
import com.clumob.segment.manager.Segment
import com.clumob.segment.manager.SegmentViewHolder
import com.clumob.segment.view.SegmentViewProvider

/**
 * Created by prashant.rathore on 07/07/18.
 */
class EmptySegment(type: Int, id: Long, val context: Context)
    : Segment(BasicController(type, id)
        , object : SegmentViewProvider {
    override fun create(parent: ViewGroup?, viewType: Int): SegmentViewHolder {
        return object : SegmentViewHolder(context, LayoutInflater.from(context), parent) {
            override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
                return View(context)
            }

            override fun onBind() {

            }

            override fun onUnBind() {
            }
        }
    }

})