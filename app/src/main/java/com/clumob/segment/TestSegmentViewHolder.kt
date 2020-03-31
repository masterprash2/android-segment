package com.clumob.segment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clumob.segment.manager.SegmentViewHolder

class TestSegmentViewHolder(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?)
    : SegmentViewHolder(context, layoutInflater, parentView) {

    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.segment_pager_item, viewGroup, false)
    }

    override fun onBind() {
    }

    override fun onUnBind() {
    }
}