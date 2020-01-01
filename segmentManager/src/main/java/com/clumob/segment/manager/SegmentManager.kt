package com.clumob.segment.manager

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import com.clumob.log.AppLog
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.controller.util.ParcelableUtil.marshall
import com.clumob.segment.controller.util.ParcelableUtil.unmarshall
import com.clumob.segment.empty.EmptySegment
import com.clumob.segment.manager.Segment.SegmentState

/**
 * Created by prashant.rathore on 23/02/18.
 */
class SegmentManager internal constructor(private val parentSegmentManager: SegmentManager?,
                                          managerId: Int,
                                          private val context: Context,
                                          val callbacks: SegmentCallbacks?,
                                          val layoutInflater: LayoutInflater) : SegmentLifecycle {

    private val mHandler = Handler()
    private var segment: Segment<*, *>? = null
    private var screenView: SegmentViewHolder<*, *>? = null
    val navigation: SegmentNavigation?

    constructor(managerId: Int, context: Context, callbacks: SegmentCallbacks?, layoutInflater: LayoutInflater)
            : this(null, managerId, context, callbacks, layoutInflater)

    val rootSegmentManager: SegmentManager
        get() = if (parentSegmentManager == null) {
            this
        } else parentSegmentManager.rootSegmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        segment = createDefaultSegmentController(savedInstanceState)
        segment!!.onCreate()
        attachSegment()
    }

    private fun attachSegment() {
        screenView = segment!!.createView(null)
        changeView(screenView!!.view, null)
        segment!!.bindView(screenView!!)
    }

    private fun createDefaultSegmentController(savedInstanceState: Bundle?): Segment<*, *> {
        val segment: Segment<*, *>
        val segmentInfo = restoreSegment(savedInstanceState)
        segment = segmentInfo?.let { createRestoreSegment(it) } ?: createEmptySegment()
        segment.attach(context, layoutInflater)
        return segment
    }

    private fun createEmptySegment(): Segment<*, *> {
        return EmptySegment(SegmentInfo(SEGMENT_ID_EMPTY, null))
    }

    protected fun restoreSegment(savedInstanceState: Bundle?): SegmentInfo? {
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

    private fun createRestoreSegment(segmentInfo: SegmentInfo): Segment<*,*> {
        return if (segmentInfo.id == SEGMENT_ID_EMPTY) createEmptySegment() else callbacks!!.provideSegment(segmentInfo)!!
    }

    fun changeSegment(segmentInfo: SegmentInfo): SegmentInfo? {
        val newController = createRestoreSegment(segmentInfo)
        newController.attach(context, layoutInflater)
        val oldController = segment
        val newScreen = newController.createView(null)
        when (oldController!!.currentState) {
            SegmentState.CREATE -> {
                newController.onCreate()
                newController.bindView(newScreen)
            }
            SegmentState.START -> {
                newController.onCreate()
                newController.bindView(newScreen)
                newController.onStart()
            }
            SegmentState.RESUME -> {
                oldController.onPause()
                newController.onCreate()
                newController.bindView(newScreen)
                newController.onStart()
                newController.onResume()
            }
            SegmentState.PAUSE -> {
                newController.onCreate()
                newController.bindView(newScreen)
                newController.onStart()
            }
            SegmentState.STOP -> {
                newController.onCreate()
                newController.bindView(newScreen)
            }
            SegmentState.DESTROY -> return segmentInfo
        }
        changeView(newScreen.view, Runnable {
            when (oldController.currentState) {
                SegmentState.CREATE -> {
                    oldController.unBindView()
                    oldController.onDestroy()
                }
                SegmentState.PAUSE, SegmentState.RESUME, SegmentState.START -> {
                    oldController.onStop()
                    oldController.unBindView()
                    oldController.onDestroy()
                }
                SegmentState.STOP -> {
                    oldController.unBindView()
                    oldController.onDestroy()
                }
                SegmentState.DESTROY -> return@Runnable
            }
        })
        segment = newController
        screenView = newScreen
        return oldController.getSegmentInfo()
    }

    protected fun changeView(newView: View?, onCompleHandler: Runnable?) {
        callbacks!!.setSegmentView(newView)
        if (onCompleHandler != null) {
            mHandler.post(onCompleHandler)
        }
    }

    override fun onStart() {
        segment!!.onStart()
    }

    override fun onResume() {
        segment!!.onResume()
    }

    fun onConfigurationChanged(newConfig: Configuration?) {
        segment!!.onConfigurationChanged(newConfig)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        segment!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        segment!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        segment!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        try {
            val segmentInfo = segment!!.getSegmentInfo()
            val marshall = marshall(segmentInfo)
            outState.putByteArray("SEGMENT_INFO", marshall)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        segment!!.onStop()
    }

    override fun handleBackPressed(): Boolean {
        return segment != null && (segment!!.handleBackPressed() || navigation != null && navigation.popBackStack())
    }

    override fun onDestroy() {
        segment!!.onDestroy()
        screenView = null
    }

    interface SegmentCallbacks {
        fun provideSegment(segmentInfo: SegmentInfo): Segment<*, *>?
        fun setSegmentView(view: View?)
        fun createSegmentNavigation(segmentManager: SegmentManager?): SegmentNavigation?
    }

    companion object {
        const val SEGMENT_ID_EMPTY = Int.MIN_VALUE
    }

    init {
        navigation = callbacks!!.createSegmentNavigation(this)
    }
}