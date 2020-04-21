package com.clumob.segment.manager

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.clumob.log.AppLog
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.controller.common.Controller
import com.clumob.segment.view.SegmentViewProvider

/**
 * Created by prashant.rathore on 02/02/18.
 */
open class Segment(protected val controller: Controller,
                   protected val screenFactory: SegmentViewProvider) : LifecycleOwner {

    var mLifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    enum class SegmentState {
        FRESH, CREATE, START, RESUME, PAUSE, STOP, DESTROY
    }

    private var boundedView: SegmentViewHolder? = null
    private lateinit var segmentInfo: SegmentInfo
    private var isBounded = false
    private var context: Context? = null
    private var layoutInflater: LayoutInflater? = null
    var currentState = SegmentState.FRESH

    fun bindSegmentInfo(segmentInfo: SegmentInfo) {
        if (isBounded && segmentInfo != this.segmentInfo)
            throw IllegalAccessException("SegmentInfo already bound. Cannot be changed")
        this.segmentInfo = segmentInfo
        isBounded = true
    }

    fun attach(context: Context, layoutInflater: LayoutInflater) {
        this.context = context
        this.layoutInflater = layoutInflater
    }

    fun getSegmentInfo(): SegmentInfo {
        return segmentInfo
    }

    fun createView(parentView: ViewGroup?): SegmentViewHolder {
        return screenFactory.create(parentView, controller.getType())
    }

    fun getBoundedView(): SegmentViewHolder? {
        return boundedView
    }

    fun onCreate() {
        when (currentState) {
            SegmentState.FRESH, SegmentState.DESTROY -> createInternal()
        }
    }

    private fun createInternal() {
        currentState = SegmentState.CREATE
        controller.onCreate()
        controller.onRestore(segmentInfo.restorableSetmentState)
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun bindView(viewHolder: SegmentViewHolder) {
        boundedView = viewHolder as SegmentViewHolder
        boundedView!!.attachLifecycleOwner(this)
        boundedView!!.bind(controller)
        //        boundedView.restoreState(segmentInfo.getSavedViewState());
    }

    fun onConfigurationChanged(newConfig: Configuration?) {
        boundedView!!.onConfigurationChanged(newConfig)
    }

    fun onStart() {
        when (currentState) {
            SegmentState.DESTROY, SegmentState.FRESH, SegmentState.CREATE -> {
                onCreate()
                startInternal()
            }
            SegmentState.STOP -> startInternal()
            SegmentState.START -> {
            }
            SegmentState.RESUME -> {
            }
            SegmentState.PAUSE -> {
            }
        }
    }

    private fun startInternal() {
        currentState = SegmentState.START
        controller.onStart()
        boundedView!!.onStart()
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun onResume() {
        if (currentState != SegmentState.RESUME) {
            onStart()
            resumeInternal()
        }
    }

    private fun resumeInternal() {
        currentState = SegmentState.RESUME
        boundedView!!.resume()
        controller.onResume()
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun onPause() {
        when (currentState) {
            SegmentState.FRESH, SegmentState.CREATE -> {
                onStart()
                pauseInternal()
            }
            SegmentState.RESUME -> pauseInternal()
            SegmentState.START, SegmentState.PAUSE, SegmentState.STOP, SegmentState.DESTROY -> {
            }
        }
    }

    private fun pauseInternal() {
        currentState = SegmentState.PAUSE
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        controller.onPause()
        boundedView!!.pause()
        val viewState = boundedView!!.createStateSnapshot()
        segmentInfo.setRestorableSegmentState(viewState)
    }

    fun onStop() {
        when (currentState) {
            SegmentState.DESTROY, SegmentState.FRESH -> onCreate()
            SegmentState.RESUME -> {
                onPause()
                stopInternal()
            }
            SegmentState.PAUSE, SegmentState.START -> stopInternal()
        }
    }

    private fun stopInternal() {
        currentState = SegmentState.STOP
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        boundedView!!.onStop()
        controller.onStop()
    }

    fun unBindView() {
        if (boundedView != null) {
            boundedView!!.detachLifecycleOwner()
            boundedView!!.unBind()
            boundedView = null
        }
    }

    fun onDestroy() {
        if (currentState != SegmentState.DESTROY) {
            onStop()
            destroyInternal()
            unBindView()
        }
    }

    private fun destroyInternal() {
        currentState = SegmentState.DESTROY
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        controller.onDestroy()
    }

    fun dettach() {
        context = null
        layoutInflater = null
    }

    fun onActivityResult(code: Int, resultCode: Int, data: Intent?) {
        boundedView!!.onActivityResult(code, resultCode, data)
    }

    fun onRequestPermissionsResult(code: Int, permissions: Array<String>, grantResults: IntArray?) {
        boundedView!!.onRequestPermissionsResult(code, permissions, grantResults!!)
    }

    fun handleBackPressed(): Boolean {
        return if (boundedView == null) {
            AppLog.d("SEGMENT", "SegmentInfo $segmentInfo")
            AppLog.printStack(NullPointerException("Cannot handle backpressed SegmentView is Null"))
            false
        } else {
            boundedView!!.handleBackPressed()
        }
    }

    val isResumed: Boolean
        get() = currentState == SegmentState.RESUME


}