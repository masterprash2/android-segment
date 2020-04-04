package com.clumob.segment.adapter

import android.content.Intent
import android.content.res.Configuration
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.viewpager.widget.PagerAdapter
import com.clumob.segment.controller.common.ItemControllerWrapper
import com.clumob.segment.manager.SegmentViewHolder

/**
 * Created by prashant.rathore on 02/07/18.
 */
abstract class SegmentPagerAdapter : PagerAdapter(), LifecycleOwner {

    var primaryItem: Page? = null
        private set
    private var parentLifecycleOwner: LifecycleOwner? = null
    private var lifecycleEventObserver: LifecycleEventObserver? = null
    private val mLifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = mLifecycleRegistry

    open fun attachLifecycleOwner(lifecycleOwner: LifecycleOwner) {
        detachLifeCycleOwner()
        this.parentLifecycleOwner = lifecycleOwner
        if (this.parentLifecycleOwner == null) {
            return
        }
        lifecycleEventObserver = LifecycleEventObserver { source, event ->
            mLifecycleRegistry.handleLifecycleEvent(event)
        }
        this.parentLifecycleOwner!!.lifecycle.addObserver(lifecycleEventObserver!!)
    }

    open fun detachLifeCycleOwner() {
        if (parentLifecycleOwner != null) {
            lifecycleEventObserver!!.onStateChanged(parentLifecycleOwner!!, Lifecycle.Event.ON_DESTROY)
            parentLifecycleOwner!!.lifecycle.removeObserver(lifecycleEventObserver!!)
        }
        parentLifecycleOwner = null
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = instantiateItemInternal(container, position)
        val segment = retrieveSegmentFromObject(item)
        val view = segment.viewHolder
        container.addView(view.view)
        view.bind(segment.controller.controller)
//        syncSegmentStateWithParent(segment)
        lifecycle.addObserver(segment)
        syncState(segment)
        return item
    }

    private fun syncState(segment: Page) {
        segment.onCreate(parentLifecycleOwner!!)
        when (parentLifecycleOwner!!.lifecycle.currentState) {
            Lifecycle.State.STARTED -> segment.onStart(parentLifecycleOwner!!)
            Lifecycle.State.RESUMED -> segment.onResume(parentLifecycleOwner!!)
        }
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, item: Any) {
        super.setPrimaryItem(container, position, item)
        val segment = retrieveSegmentFromObject(item)
        if (primaryItem !== segment) {
            val oldPrimaryItem = primaryItem
            primaryItem = segment
            oldPrimaryItem?.onPause(this)
            primaryItem?.onResume(this)
        }
    }

    protected abstract fun retrieveSegmentFromObject(item: Any): Page

    override fun registerDataSetObserver(observer: DataSetObserver) {
        super.registerDataSetObserver(observer)
    }

    override fun getItemPosition(item: Any): Int {
        return computeItemPosition(item)
    }

    open fun computeItemPosition(item: Any): Int {
        return POSITION_UNCHANGED
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        val segment = retrieveSegmentFromObject(item)
        val view = segment.viewHolder.view
        segment.onStop(parentLifecycleOwner!!)
        lifecycle.removeObserver(segment)
        destroyItem(item)
        container.removeView(view)
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        val segment = retrieveSegmentFromObject(item)
        val boundedView = segment.viewHolder
        return boundedView.view === view
    }

    abstract fun instantiateItemInternal(container: ViewGroup, position: Int): Any

    open fun destroyItem(item: Any) {
        val segment = retrieveSegmentFromObject(item)
        segment.viewHolder.unBind()
        if (primaryItem === segment) {
            primaryItem = null
        }
    }

    fun handleBackPressed(): Boolean {
        return if (primaryItem == null) {
            false
        } else primaryItem!!.viewHolder.handleBackPressed()
    }

    fun onActivityResult(code: Int, resultCode: Int, data: Intent?) {
        if (primaryItem != null) {
            primaryItem!!.viewHolder.onActivityResult(code, resultCode, data)
        }
    }

    open fun onConfigurationChanged(configuration: Configuration?) {
        if (primaryItem != null) {
            primaryItem!!.viewHolder.onConfigurationChanged(configuration)
        }
    }

    fun onRequestPermissionsResult(code: Int, permissions: Array<String>, grantResults: IntArray?) {
        if (primaryItem != null) primaryItem!!.viewHolder.onRequestPermissionsResult(code, permissions, grantResults)
    }

    class Page(val controller: ItemControllerWrapper,
               val viewHolder: SegmentViewHolder,
               val adapter: SegmentPagerAdapter) : LifecycleOwner, DefaultLifecycleObserver {

        val mLifecycleRegistry = LifecycleRegistry(this)

        init {
            viewHolder.attachLifecycleOwner(this)
        }

        override fun onCreate(owner: LifecycleOwner) {
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        override fun onStart(owner: LifecycleOwner) {
            when (adapter.parentLifecycleOwner!!.lifecycle.currentState) {
                Lifecycle.State.INITIALIZED,
                Lifecycle.State.CREATED,
                Lifecycle.State.STARTED,
                Lifecycle.State.RESUMED -> onCreate(owner)
            }

            when (controller.state) {
                ItemControllerWrapper.State.CREATE,
                ItemControllerWrapper.State.STOP -> {
                    mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
                    controller.performStart(this)
                }
            }
        }

        override fun onResume(owner: LifecycleOwner) {
            if (this == adapter.primaryItem) {
                when (adapter.parentLifecycleOwner!!.lifecycle.currentState) {
                    Lifecycle.State.STARTED,
                    Lifecycle.State.CREATED,
                    Lifecycle.State.INITIALIZED,
                    Lifecycle.State.RESUMED -> onStart(owner)
                }

                when (controller.state) {
                    ItemControllerWrapper.State.START,
                    ItemControllerWrapper.State.PAUSE -> {
                        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
                        controller.performResume()
                    }
                }
            } else onPause(owner)
        }

        override fun onPause(owner: LifecycleOwner) {
            when (controller.state) {
                ItemControllerWrapper.State.RESUME -> {
                    mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                    controller.performPause()
                }
            }
        }

        override fun onStop(owner: LifecycleOwner) {

            if (controller.state == ItemControllerWrapper.State.RESUME) {
                onPause(owner)
            }

            when (controller.state) {
                ItemControllerWrapper.State.RESUME,
                ItemControllerWrapper.State.PAUSE,
                ItemControllerWrapper.State.START -> {
                    mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
                    controller.performStop(this)
                }
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }

        override fun getLifecycle(): Lifecycle {
            return mLifecycleRegistry
        }

    }
}