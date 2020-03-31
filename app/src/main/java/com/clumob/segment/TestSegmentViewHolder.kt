package com.clumob.segment

import android.content.Context
import android.util.Log
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
        Log.d("SEGMENTSUB", "OnBind -" + this.controller.toString().split("@").toTypedArray()[1])
    }

    override fun onUnBind() {
        Log.d("SEGMENTSUB", "OnUnBind -" + this.controller.toString().split("@").toTypedArray()[1])
    }
}