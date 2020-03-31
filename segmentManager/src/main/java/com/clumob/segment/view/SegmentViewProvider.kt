package com.clumob.segment.view

import android.view.ViewGroup
import com.clumob.segment.controller.common.Controller
import com.clumob.segment.manager.SegmentViewHolder

/**
 * Created by prashant.rathore on 28/05/18.
 */
interface SegmentViewProvider {
    fun create(parent: ViewGroup?, viewType: Int): SegmentViewHolder
}