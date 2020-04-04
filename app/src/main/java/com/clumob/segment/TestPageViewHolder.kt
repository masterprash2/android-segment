package com.clumob.segment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clumob.segment.adapter.RvAdapter
import com.clumob.segment.controller.common.Controller
import com.clumob.segment.controller.list.ArraySource
import com.clumob.segment.controller.list.MultiplexSource
import com.clumob.segment.manager.SegmentViewHolder

class TestPageViewHolder(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?)
    : SegmentViewHolder(context, layoutInflater, parentView) {

    private lateinit var recyclerView: RecyclerView

    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.segment_sub_item, viewGroup, false)
    }

    override fun onBind() {
        recyclerView = view.findViewById(R.id.recyclerView)
        this.view.setBackgroundColor(java.util.Random().nextInt(Integer.MAX_VALUE))
        Log.d("SEGMENTPAGE", "OnBind -" + this.getController<Controller>().toString().split("@").toTypedArray()[1])
        val multiplexSource = MultiplexSource()
        multiplexSource.addSource(createArraySource())
        recyclerView.adapter = RvAdapter(RvViewHolderProvider(context, layoutInflater), multiplexSource, this)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun createArraySource(): ArraySource<Controller> {
        val source = ArraySource<Controller>()
        source.setItems(createItems())
        return source
    }

    private fun createItems(): List<Controller>? {
        val list = ArrayList<TestRecyclerItem>()
        for (i in 0..1) list.add(createItem(i))
        return list
    }


    private fun createItem(index: Int): TestRecyclerItem {
        return TestRecyclerItem(index)
    }

    override fun onUnBind() {
        recyclerView.adapter = null
        Log.d("SEGMENTPAGE", "OnUnBind -" + this.getController<Controller>().toString().split("@").toTypedArray()[1])
    }
}