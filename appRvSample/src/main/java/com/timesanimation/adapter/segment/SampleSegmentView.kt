package com.timesanimation.adapter.segment

import com.timesanimation.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clumob.segment.controller.SegmentController
import com.clumob.segment.manager.SegmentViewHolder

/**
 * Created by prashant.rathore on 11/07/18.
 */
class SampleSegmentView(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?)
    : SegmentViewHolder(context, layoutInflater, parentView) {
    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.item_card_fill, viewGroup, false)
    }

    override fun onBind() {}
    override fun onUnBind() {}
}