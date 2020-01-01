package com.clumob.segment.manager

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

interface SegmentViewHolderFactory {
    fun create(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?): SegmentViewHolder<*, *>
}