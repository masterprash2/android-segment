package com.clumob.segment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.clumob.segment.controller.common.Controller
import com.clumob.segment.manager.SegmentViewHolder
import java.util.*

class TestRecyclerItemView(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?)
    : SegmentViewHolder(context, layoutInflater, parentView) {
    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
        val inflate = layoutInflater.inflate(R.layout.recyeritem, viewGroup, false)
        inflate.setBackgroundColor(Random().nextInt(Integer.MAX_VALUE))
        return inflate
    }

    override fun onBind() {
        Log.d("SEGMENTRV", "OnBind ${this.getController<Controller>().getId()} - " + this.getController<Controller>().toString().split("@").toTypedArray()[1])
        (view as TextView).apply {
            setText("Index = " + getController<TestRecyclerItem>().index)
        }
    }

    override fun onUnBind() {
        Log.d("SEGMENTRV", "OnUnBind ${this.getController<Controller>().getId()} - " + this.getController<Controller>().toString().split("@").toTypedArray()[1])
    }
}