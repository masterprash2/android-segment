package com.clumob.segment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clumob.segment.controller.SegmentControllerImpl
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.manager.*
import com.clumob.segment.support.appcompact.SegmentAppCompatActivity

class MainActivity : SegmentAppCompatActivity() {
    override fun provideSegment(segmentInfo: SegmentInfo): Segment {
        val segment = Segment(SegmentControllerImpl(), object : SegmentViewHolderFactory {
            override fun create(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?): SegmentViewHolder {
                return TestSegmentScreenHolder(context, layoutInflater, parentView)
            }
        })
        segment.bindSegmentInfo(segmentInfo)
        return segment
    }

    @SuppressLint("NewApi")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val segmentView = SegmentView(context = this)
        val segmentInfo = SegmentInfo(1, null)
        val segment = Segment(SegmentControllerImpl(), object : SegmentViewHolderFactory {
            override fun create(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?): SegmentViewHolder {
                return TestSegmentScreenHolder(context, layoutInflater, parentView)
            }
        })
        segment.bindSegmentInfo(segmentInfo)
        segmentView.setSegment(segment)
        setContentView(segmentView)
        //        getSegmentManager().getNavigation().navigateToScreen(new SegmentInfo<Storable, Storable>(1, null));
    }

    override fun setSegmentView(view: View) {
        setContentView(view)
    }

    override fun createSegmentNavigation(segmentManager: SegmentManager): SegmentNavigation {
        return SegmentNavigation(segmentManager)
    }
}