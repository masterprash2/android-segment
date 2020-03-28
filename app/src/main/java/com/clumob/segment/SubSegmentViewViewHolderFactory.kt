package com.clumob.segment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.clumob.segment.controller.SegmentController
import com.clumob.segment.manager.SegmentViewHolder
import com.clumob.segment.manager.SegmentViewHolderFactory
import java.util.*

/**
 * Created by prashant.rathore on 08/07/18.
 */
internal class SubSegmentViewViewHolderFactory : SegmentViewHolderFactory {
    override fun create(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?): SegmentViewHolder<*> {
        return object : SegmentViewHolder<SegmentController?>(context, layoutInflater, parentView) {
            override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
                return layoutInflater.inflate(R.layout.segment_sub_item, viewGroup, false)
            }

            override fun onBind() {
                val viewById = view.findViewById<TextView>(R.id.subText)
                viewById.text = Random().nextInt().toString()
            }

            override fun onUnBind() {}
        }
    }
}