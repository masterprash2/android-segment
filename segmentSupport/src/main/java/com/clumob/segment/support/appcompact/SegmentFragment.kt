package com.clumob.segment.support.appcompact

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.manager.Segment
import com.clumob.segment.manager.SegmentManager
import com.clumob.segment.manager.SegmentManager.SegmentCallbacks
import com.clumob.segment.manager.SegmentNavigation
import com.clumob.segment.manager.SegmentViewHolder

/**
 * Created by prashant.rathore on 14/02/18.
 */
abstract class SegmentFragment : Fragment(), SegmentCallbacks {
    private var segment: Segment? = null
    private var viewHolder: SegmentViewHolder? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        segment = createSegment()
        segment!!.onCreate()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        segment!!.attach(context!!, inflater)
        viewHolder = segment!!.createView(container)
        return viewHolder!!.view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnKeyListener { view, i, keyEvent -> segment!!.handleBackPressed() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        segment!!.bindView(viewHolder!!)
    }

    override fun onStart() {
        super.onStart()
        segment!!.onStart()
    }

    override fun onResume() {
        if (userVisibleHint) {
            segment!!.onResume()
        }
        super.onResume()
    }

    override fun onPause() {
        segment!!.onPause()
        super.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        segment!!.onConfigurationChanged(newConfig)
        super.onConfigurationChanged(newConfig)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (view == null) {
            return
        }
        if (userVisibleHint) {
            segment!!.onResume()
        } else {
            segment!!.onPause()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        segment!!.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        this.view!!.setOnKeyListener(null)
        segment!!.unBindView()
        viewHolder = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        segment!!.onDestroy()
        super.onDestroy()
    }

    override fun onDetach() {
        segment = null
        super.onDetach()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        segment!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        segment!!.onActivityResult(requestCode, resultCode, data)
    }

    protected fun createSegment(): Segment? {
        return provideSegment(provideSegmentInfo()!!)
    }

    protected abstract fun provideSegmentInfo(): SegmentInfo?
    override fun setSegmentView(view: View) {
        throw UnsupportedOperationException()
    }

    override fun createSegmentNavigation(segmentManager: SegmentManager): SegmentNavigation {
        return SegmentNavigation(segmentManager)
    }
}