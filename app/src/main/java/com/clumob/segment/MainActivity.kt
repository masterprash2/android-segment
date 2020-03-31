package com.clumob.segment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clumob.segment.activity.SegmentAppCompatActivity
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.manager.*
import com.clumob.segment.view.SegmentViewProvider

class MainActivity : SegmentAppCompatActivity() {
    override fun provideSegment(segmentInfo: SegmentInfo): Segment {
        val context = this
        val segment = Segment(SubSegmentController(), object : SegmentViewProvider {
            override fun create(parent: ViewGroup?, viewType: Int): SegmentViewHolder {
                return TestSegmentViewHolder(context, LayoutInflater.from(context), parent)
            }
        })
        segment.bindSegmentInfo(segmentInfo)
        return segment
    }

    @SuppressLint("NewApi")
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val segmentView = SegmentView(context = this)
//        val segmentInfo = SegmentInfo(1, null)
//        val segment = Segment(SubSegmentController(), object : SegmentViewProvider {
//
//            override fun create(parent: ViewGroup?, viewType: Int): SegmentViewHolder {
//                return TestSegmentViewHolder(parent!!.context, layoutInflater, parent)
//            }
//        })
//        segment.bindSegmentInfo(segmentInfo)
//        segmentView.setSegment(segment)
//        setContentView(segmentView)
        segmentManager!!.navigation.navigateToScreen(SegmentInfo(1,null))
    }

    override fun setSegmentView(view: View) {
        setContentView(view)
    }

    override fun onRestart() {
        super.onRestart()
        segmentManager!!.navigation.navigateToScreen(SegmentInfo(1,null))
    }

    override fun createSegmentNavigation(segmentManager: SegmentManager): SegmentNavigation {
        return SegmentNavigation(segmentManager)
    }
}