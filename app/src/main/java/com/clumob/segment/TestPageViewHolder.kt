package com.clumob.segment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.clumob.segment.controller.common.Controller
import com.clumob.segment.manager.SegmentViewHolder
import kotlin.random.Random

class TestPageViewHolder(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?)
    : SegmentViewHolder(context, layoutInflater, parentView) {

    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.segment_sub_item, viewGroup, false)
    }

    override fun onBind() {
        this.view.setBackgroundColor(Random(Integer.MAX_VALUE).nextInt())
        Log.d("SEGMENTPAGE", "OnBind -" + this.getController<Controller>().toString().split("@").toTypedArray()[1])
    }

    override fun onUnBind() {
        Log.d("SEGMENTPAGE", "OnUnBind -" + this.getController<Controller>().toString().split("@").toTypedArray()[1])
    }
}