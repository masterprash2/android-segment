package com.clumob.segment.view

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.clumob.segment.controller.common.ItemControllerWrapper
import com.clumob.segment.manager.SegmentViewHolder

/**
 * Created by prashant.rathore on 28/05/18.
 */
open class RvViewHolder(private val viewHolder: SegmentViewHolder) : RecyclerView.ViewHolder(viewHolder.view) {

    private var controller: ItemControllerWrapper? = null

    fun <T : ItemControllerWrapper> getController(): T = controller as T

    private var isScreenResumed = false
    private var isScreenStared = false
    private var isBounded = false
    var parentLifecycleOwner: LifecycleOwner? = null
        private set
    private var lifecycleObserver: DefaultLifecycleObserver? = null

    internal fun bind(controller: ItemControllerWrapper) {
        if (isBounded) {
            unBind()
        }
        this.controller = controller
        bindView()
        if (lifecycleObserver == null) observeLifecycle()
        isBounded = true
    }

    private fun bindView() {
        val currentState = parentLifecycleOwner!!.lifecycle.currentState
        viewHolder.bind(controller!!.controller)
        when(currentState) {
            Lifecycle.State.STARTED -> {
                updateScreenStartState(true)
            }
            Lifecycle.State.RESUMED -> {
                updateScreenStartState(true)
                updateScreenResumeState(true)
            }
        }
    }

    internal fun performAttachToWindow() {
        onAttachedToWindow()
        if (isScreenStared) {
            onScreenStarted()
            if (isScreenResumed) {
                onScreenResumed()
            }
        }
    }

    protected open fun onAttachedToWindow() {}

    internal fun performDetachFromWindow() {
        onScreenStopped()
        onDetachedFromWindow()
    }


    protected open fun onDetachedFromWindow() {}

    fun unBind() {
        unBindView()
        performDetachFromWindow()
        controller = null
        removeLifecycleObserver()
        isScreenResumed = false
        isScreenStared = false
        isBounded = false
    }

    private fun unBindView() {
        viewHolder.unBind()
    }

    private fun observeLifecycle() {
        removeLifecycleObserver()
        lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {}
            override fun onStart(owner: LifecycleOwner) {
                updateScreenStartState(true)
            }

            override fun onResume(owner: LifecycleOwner) {
                updateScreenResumeState(true)
            }

            override fun onPause(owner: LifecycleOwner) {
                updateScreenResumeState(false)
            }

            override fun onStop(owner: LifecycleOwner) {
                updateScreenStartState(false)
            }

            override fun onDestroy(owner: LifecycleOwner) {}
        }
        parentLifecycleOwner!!.lifecycle.addObserver(lifecycleObserver!!)

    }

    private fun updateScreenStartState(isStarted: Boolean) {
        this.isScreenStared = isStarted
        if (isScreenStared) onScreenStarted()
        else onScreenStopped()
    }

    private fun onScreenStarted() {
        when (controller?.state) {
            ItemControllerWrapper.State.CREATE,
            ItemControllerWrapper.State.STOP -> controller!!.performStart(this)
        }
    }

    private fun updateScreenResumeState(isInFocus: Boolean) {
        isScreenResumed = isInFocus
        if (isScreenResumed) {
            onScreenResumed()
        } else {
            onScreenPaused()
        }
    }

    protected open fun onScreenResumed() {
        when (controller?.state) {
            ItemControllerWrapper.State.START,
            ItemControllerWrapper.State.RESUME,
            ItemControllerWrapper.State.PAUSE -> controller!!.performResume()
        }
    }

    private fun onScreenStopped() {
        onScreenPaused()
        when (controller?.state) {
            ItemControllerWrapper.State.START,
            ItemControllerWrapper.State.PAUSE -> controller!!.performStop(this)
        }
    }

    protected open fun onScreenPaused() {
        when (controller?.state) {
            ItemControllerWrapper.State.RESUME,
            ItemControllerWrapper.State.PAUSE -> controller!!.performPause()
        }
    }

    fun setLifecycleOwner(lifecycle: LifecycleOwner) {
        if (parentLifecycleOwner !== lifecycle) {
            removeLifecycleObserver()
            parentLifecycleOwner = lifecycle
            viewHolder.attachLifecycleOwner(parentLifecycleOwner)
            observeLifecycle()
        }
    }

    private fun removeLifecycleObserver() {
        if (parentLifecycleOwner != null && lifecycleObserver != null) {
            parentLifecycleOwner!!.lifecycle.removeObserver(lifecycleObserver!!)
            viewHolder.detachLifecycleOwner()
            lifecycleObserver = null
        }
    }

}