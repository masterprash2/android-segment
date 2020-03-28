package com.clumob.segment.manager

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import com.clumob.segment.controller.SegmentController
import com.clumob.segment.controller.Storable
import com.clumob.segment.manager.SegmentManager.SegmentCallbacks
import java.util.*

abstract class SegmentViewHolder<Controller : SegmentController?>
(
        val context: Context,
        val layoutInflater: LayoutInflater,
        parentView: ViewGroup?
) : LifecycleOwner {


    enum class SegmentViewState {
        FRESH, CREATE, START, RESUME, PAUSE, STOP, DESTROY
    }


    val mLifecycleRegistry = LifecycleRegistry(this)
    private var savedInstance: Bundle? = null

    private var parentLifecycleOwner: LifecycleOwner? = null
    private var parentLifecycleObserver: LifecycleObserver? = null


    var controller: Controller? = null
        private set

    private val segmentLifecycleListeners: MutableList<SegmentLifecycle> = LinkedList()
    private var currentState = SegmentViewState.FRESH
    var isAttachedToWindow = false
    private val segmentManagers = LinkedHashMap<Int, SegmentManager>()


     val view: View by lazy(LazyThreadSafetyMode.PUBLICATION) {
         val view = createView(layoutInflater, parentView)

         view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
             override fun onViewAttachedToWindow(view: View) {
                 isAttachedToWindow = true
                 onAttachedToWindow()
             }

             override fun onViewDetachedFromWindow(view: View) {
                 isAttachedToWindow = false
                 onDetachedFromWindow()
             }
         })
         view
     }


    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    fun onConfigurationChanged(newConfig: Configuration?) {
        for (segmentManager in segmentManagers.values)
            segmentManager.onConfigurationChanged(newConfig)
    }

    fun attachLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        detachLifecycleOwner()
        if (lifecycleOwner == null) {
            return
        }
        parentLifecycleOwner = lifecycleOwner
        parentLifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            }

            override fun onStart(owner: LifecycleOwner) {
                mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            }

            override fun onResume(owner: LifecycleOwner) {
                mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            }

            override fun onPause(owner: LifecycleOwner) {
                mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            }

            override fun onStop(owner: LifecycleOwner) {
                mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            }
        }
        parentLifecycleOwner!!.lifecycle.addObserver(parentLifecycleObserver!!)
    }

    fun detachLifecycleOwner() {
        if (parentLifecycleOwner != null) {
            parentLifecycleOwner!!.lifecycle.removeObserver(parentLifecycleObserver!!)
        }
        parentLifecycleOwner = null
        parentLifecycleObserver = null
    }



    protected fun onAttachedToWindow() {}
    protected fun onDetachedFromWindow() {}

    protected abstract fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View


    internal fun bind(controller: Controller) {
        currentState = SegmentViewState.CREATE
        this.controller = controller
        for (lifecycle in segmentLifecycleListeners) {
            lifecycle.onCreate(null)
        }
        onBind()
    }

    protected abstract fun onBind()

    internal fun onStart() {
        currentState = SegmentViewState.START
        for (lifecycle in segmentLifecycleListeners) {
            lifecycle.onStart()
        }
    }

    internal fun resume() {
        currentState = SegmentViewState.RESUME
        for (lifecycle in segmentLifecycleListeners) {
            lifecycle.onResume()
        }
    }

    internal fun pause() {
        currentState = SegmentViewState.PAUSE
        for (lifecycle in segmentLifecycleListeners) {
            lifecycle.onPause()
        }
    }

    fun createStateSnapshot(): Storable? {
        return null
    }

    internal fun onStop() {
        currentState = SegmentViewState.STOP
        for (lifecycle in segmentLifecycleListeners) {
            lifecycle.onStop()
        }
    }

    internal fun unBind() {
        currentState = SegmentViewState.DESTROY
        for (lifecycle in segmentLifecycleListeners) {
            lifecycle.onDestroy()
        }
        onUnBind()
    }

    protected abstract fun onUnBind()

    fun onActivityResult(code: Int, resultCode: Int, data: Intent?) {
        for (segmentManager in segmentManagers.values) {
            segmentManager.onActivityResult(code, resultCode, data)
        }
    }

    fun onRequestPermissionsResult(code: Int, permissions: Array<String>, grantResults: IntArray) {
        for (segmentManager in segmentManagers.values) {
            segmentManager.onRequestPermissionsResult(code, permissions, grantResults)
        }
    }

    fun getNavigation(navigationId: Int): SegmentNavigation {
        var segmentManager = segmentManagers[navigationId]
        if (segmentManager == null) {
            segmentManager = createManagerInternal(navigationId, null)
        }
        return segmentManager.navigation
    }

    open fun createChildManagerCallbacks(navigationId: Int): SegmentCallbacks? {
        return null
    }

    private fun createManagerInternal(managerId: Int, savedInstance: Bundle?): SegmentManager {
        val manager = SegmentManager(null, context, createChildManagerCallbacks(managerId)!!, layoutInflater)
        segmentManagers[managerId] = manager
        this.savedInstance = savedInstance
        when (currentState) {
            SegmentViewState.FRESH -> {
            }
            SegmentViewState.CREATE -> manager.onCreate(savedInstance)
            SegmentViewState.START -> {
                manager.onCreate(savedInstance)
                manager.onStart()
            }
            SegmentViewState.RESUME -> {
                manager.onCreate(savedInstance)
                manager.onStart()
                manager.onResume()
            }
            SegmentViewState.PAUSE -> {
                manager.onCreate(savedInstance)
                manager.onStart()
            }
            SegmentViewState.STOP -> manager.onCreate(savedInstance)
            SegmentViewState.DESTROY -> {
            }
        }
        registerLifecycleListener(manager)
        return manager
    }

    fun registerLifecycleListener(listener: SegmentLifecycle) {
        segmentLifecycleListeners.add(0, listener)
        when (currentState) {
            SegmentViewState.FRESH -> {
            }
            SegmentViewState.STOP, SegmentViewState.CREATE -> listener.onCreate(savedInstance)
            SegmentViewState.PAUSE, SegmentViewState.START -> listener.onStart()
            SegmentViewState.RESUME -> listener.onResume()
            SegmentViewState.DESTROY -> listener.onDestroy()
        }
    }

    fun unRegisterLifecycleListener(lifecycle: SegmentLifecycle) {
        segmentLifecycleListeners.remove(lifecycle)
        lifecycle.onDestroy()
    }

    open fun handleBackPressed(): Boolean {
        for (lifecycle in segmentLifecycleListeners) {
            if (lifecycle.handleBackPressed()) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val KEY_SAVE_STATE = "saveViewState"
    }

}