package com.clumob.segment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.clumob.segment.adapter.SegmentStatePagerAdapter
import com.clumob.segment.controller.common.Controller
import com.clumob.segment.controller.list.ArraySource
import com.clumob.segment.controller.list.ItemControllerSource
import com.clumob.segment.manager.SegmentViewHolder

class TestSegmentViewHolder(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?)
    : SegmentViewHolder(context, layoutInflater, parentView) {

    val pager: ViewPager by lazy {
        view.findViewById(R.id.pager) as ViewPager
    }

    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.segment_pager_item, viewGroup, false)
    }

    override fun onBind() {
        Log.d("SEGMENTSUB", "OnBind ${this.getController<Controller>().getId()} -" + this.getController<Controller>().toString().split("@").toTypedArray()[1])
        pager.adapter = SegmentStatePagerAdapter(createSource(), ScreenProvider(context, layoutInflater)).apply {
            attachLifecycleOwner(this@TestSegmentViewHolder)
        }
    }

    private fun createSource(): ItemControllerSource {
        return ArraySource<Controller>().apply {
            setItems(createItems())
        }
    }

    private fun createItems(): List<Controller> {
        return listOf(PageController(),PageController(),PageController(),PageController())
    }

    override fun onUnBind() {
        Log.d("SEGMENTSUB", "OnUnBind ${this.getController<Controller>().getId()} - " + this.getController<Controller>().toString().split("@").toTypedArray()[1])
        (pager.adapter as SegmentStatePagerAdapter).detachLifeCycleOwner()
    }
}