package com.clumob.segment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        (view as TextView).apply {
            setText("Index = " + getController<TestRecyclerItem>().index)
        }
    }

    override fun onUnBind() {
    }
}