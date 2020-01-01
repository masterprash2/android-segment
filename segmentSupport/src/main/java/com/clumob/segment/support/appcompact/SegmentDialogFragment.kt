package com.clumob.segment.support.appcompact

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.clumob.log.AppLog
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.controller.util.ParcelableUtil.marshall
import com.clumob.segment.controller.util.ParcelableUtil.unmarshall
import com.clumob.segment.manager.Segment
import com.clumob.segment.manager.SegmentManager
import com.clumob.segment.manager.SegmentManager.SegmentCallbacks
import com.clumob.segment.manager.SegmentNavigation
import com.clumob.segment.manager.SegmentViewHolder

/**
 * Created by prashant.rathore on 14/02/18.
 */
abstract class SegmentDialogFragment : DialogFragment(), SegmentCallbacks, DialogInterface.OnKeyListener {
    private var segment: Segment<*, *>? = null
    private var viewHolder: SegmentViewHolder<*, *>? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preSegmentCreate(savedInstanceState)
        segment = createSegment(savedInstanceState)
        segment!!.onCreate()
    }

    protected fun preSegmentCreate(savedInstanceState: Bundle?) {}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        segment!!.attach(context!!, inflater)
        viewHolder = segment!!.createView(container)
        return viewHolder!!.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        segment!!.bindView(viewHolder!!)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        segment!!.onConfigurationChanged(newConfig)
        super.onConfigurationChanged(newConfig)
    }

    override fun onStart() {
        super.onStart()
        segment!!.onStart()
    }

    override fun onResume() {
        segment!!.onResume()
        dialog!!.setOnKeyListener(this)
        super.onResume()
    }

    override fun onKey(dialogInterface: DialogInterface, i: Int, keyEvent: KeyEvent): Boolean {
        return if (keyEvent.keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) {
            segment!!.handleBackPressed()
        } else false
    }

    override fun onPause() {
        segment!!.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        try {
            val segmentInfo = segment!!.getSegmentInfo()
            val marshall = marshall(segmentInfo)
            outState.putByteArray("SEGMENT_INFO", marshall)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        segment!!.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
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

    protected fun restoreSegmentInfo(savedInstanceState: Bundle?): SegmentInfo? {
        var segmentInfoBytes: ByteArray? = null
        if (savedInstanceState == null) { //            segmentInfoBytes = getIntent().getByteArrayExtra("SEGMENT_INFO");
        } else {
            segmentInfoBytes = savedInstanceState.getByteArray("SEGMENT_INFO")
        }
        var segmentInfo: SegmentInfo? = null
        try {
            if (segmentInfoBytes != null) {
                segmentInfo = unmarshall(segmentInfoBytes, SegmentInfo.CREATOR)
            }
        } catch (e: Exception) {
            AppLog.printStack(e)
        }
        return segmentInfo
    }

    protected fun createSegment(savedInstanceState: Bundle?): Segment<*, *>? {
        val segmentInfo = restoreSegmentInfo(savedInstanceState)
        return provideSegment(segmentInfo ?: provideSegmentInfo()!!)
    }

    protected abstract fun provideSegmentInfo(): SegmentInfo?
    override fun setSegmentView(view: View?) {
        throw UnsupportedOperationException()
    }

    override fun createSegmentNavigation(segmentManager: SegmentManager?): SegmentNavigation? {
        return null
    }
}