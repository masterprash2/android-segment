package com.clumob.segment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.clumob.segment.manager.SegmentViewHolder
import com.clumob.segment.view.SegmentViewProvider

class ScreenProvider(val context: Context, val layoutInflater: LayoutInflater) : SegmentViewProvider {
    override fun create(parent: ViewGroup?, viewType: Int): SegmentViewHolder {
        return TestPageViewHolder(context, layoutInflater, parent)
    }
}