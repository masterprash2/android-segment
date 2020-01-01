package com.clumob.segment.manager

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.clumob.segment.manager.Segment.SegmentState

class SegmentView : FrameLayout {
    private var segment: Segment<*, *>? = null
    private var segmentState = SegmentState.FRESH

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    fun setSegment(segment: Segment<*, *>) {
        if (this.segment !== segment) {
            if (this.segment != null) {
                if (segment.getBoundedView() != null) removeView(segment.getBoundedView()!!.view)
                segment.onDestroy()
            }
            this.segment = segment
            if (this.segment != null) {
                this.segment!!.attach(context, LayoutInflater.from(context))
                this.segment!!.onCreate()
                val view = this.segment!!.createView(this)
                this.segment!!.bindView(view)
                addView(view.view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                updateSegmentState(segmentState)
            }
        }
    }

    override fun onAttachedToWindow() {
        updateSegmentState(SegmentState.RESUME)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        updateSegmentState(SegmentState.PAUSE)
        super.onDetachedFromWindow()
    }

    private fun updateSegmentState(state: SegmentState) {
        segmentState = state
        if (segment == null) {
            return
        }
        when (state) {
            SegmentState.FRESH, SegmentState.CREATE -> segment!!.onCreate()
            SegmentState.START -> segment!!.onStart()
            SegmentState.RESUME -> segment!!.onResume()
            SegmentState.PAUSE -> segment!!.onPause()
            SegmentState.STOP -> segment!!.onStop()
            SegmentState.DESTROY -> segment!!.onDestroy()
        }
    }
}