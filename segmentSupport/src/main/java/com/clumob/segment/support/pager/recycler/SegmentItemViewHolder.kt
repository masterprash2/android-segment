package com.clumob.segment.support.pager.recycler

import android.view.View
import com.clumob.recyclerview.adapter.RvViewHolder
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.controller.list.SegmentItemController
import com.clumob.segment.manager.Segment
import com.clumob.segment.manager.SegmentViewHolder

/**
 * Created by prashant.rathore on 11/07/18.
 */
abstract class SegmentItemViewHolder(val view: View, private val segmentViewHolder: SegmentViewHolder) : RvViewHolder<SegmentItemController>(view) {
    var segment: Segment? = null
        private set
    private var segmentInfo: SegmentInfo? = null
    private var isAttached = false
    override fun bindView() {
        if (segment == null || segmentInfo !== controller!!.segmentInfo()) {
            destroySegment()
            segmentInfo = controller!!.segmentInfo()
            segment = createSegment(segmentInfo!!)
            segment!!.bindView(segmentViewHolder)
        }
        onBindSegment()
    }

    protected abstract fun createSegment(segmentInfo: SegmentInfo) : Segment

    override fun onAttached() {
        isAttached = true
        super.onAttached()
        segment!!.onStart()
        resume()
    }

    protected abstract fun onBindSegment()
    override fun unBindView() {
        onUnbindSegment()
        destroySegment()
    }

    protected abstract fun onUnbindSegment()
    override fun onDetached() {
        isAttached = false
        pause()
        segment!!.onStop()
        super.onDetached()
    }

    private fun destroySegment() {
        segment?.onDestroy()
        segmentInfo = null
        segment = null
    }

    override fun onScreenIsInFocus() {
        super.onScreenIsInFocus()
        resume()
    }

    override fun onScreenIsOutOfFocus() {
        pause()
        super.onScreenIsOutOfFocus()
    }

    fun resume() {
        if (!isResumed && isAttached) {
            segment!!.onResume()
        }
    }

    val isResumed: Boolean
        get() = segment != null && segment!!.isResumed

    fun pause() {
        if (isResumed) {
            segment!!.onPause()
        }
    }

    fun handleBackPressed(): Boolean {
        return segmentViewHolder.handleBackPressed()
    }

}