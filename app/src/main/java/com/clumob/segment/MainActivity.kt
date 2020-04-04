package com.clumob.segment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
        segmentManager!!.navigation.navigateToScreen(SegmentInfo(1,null))
    }

    override fun setSegmentView(view: View) {
        setContentView(view)
    }

    override fun onRestart() {
        super.onRestart()
        segmentManager!!.navigation.navigateToScreen(SegmentInfo(1,null))
    }

    override fun onPause() {
        Log.d("SEGMENT"," Activity Pause Begin")
        super.onPause()
        Log.d("SEGMENT"," Activity Pause end")
    }

    override fun onStop() {
        Log.d("SEGMENT"," Activity Stop")
        super.onStop()
        Log.d("SEGMENT"," Activity Stop End")
    }

    override fun onDestroy() {
        Log.d("SEGMENT"," Activity Destroy Start")
        super.onDestroy()
        Log.d("SEGMENT"," Activity Destroy End")
    }

    override fun createSegmentNavigation(segmentManager: SegmentManager): SegmentNavigation {
        return SegmentNavigation(segmentManager)
    }
}