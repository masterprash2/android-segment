package com.clumob.segment.support.pager.viewpager

import android.content.Intent
import android.content.res.Configuration
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.PagerAdapter
import com.clumob.segment.manager.Segment
import com.clumob.segment.manager.SegmentViewHolder.SegmentViewState

/**
 * Created by prashant.rathore on 02/07/18.
 */
abstract class SegmentPagerAdapter : PagerAdapter() {
    var primaryItem: Segment< *>? = null
        private set
    private var parentState = SegmentViewState.FRESH
    private var lifecycleOwner: LifecycleOwner? = null
    private var lifecycleEventObserver: LifecycleEventObserver? = null
    fun attachLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        detachLifeCycleOwner()
        this.lifecycleOwner = lifecycleOwner
        if (this.lifecycleOwner == null) {
            return
        }
        lifecycleEventObserver = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> onCreate()
                Lifecycle.Event.ON_START -> onStart()
                Lifecycle.Event.ON_RESUME -> onResume()
                Lifecycle.Event.ON_PAUSE -> onPause()
                Lifecycle.Event.ON_STOP -> onStop()
                Lifecycle.Event.ON_DESTROY -> onDestroy()
                Lifecycle.Event.ON_ANY -> {
                }
            }
        }
        this.lifecycleOwner!!.lifecycle.addObserver(lifecycleEventObserver!!)
    }

    fun detachLifeCycleOwner() {
        if (lifecycleOwner != null) {
            lifecycleEventObserver!!.onStateChanged(lifecycleOwner!!, Lifecycle.Event.ON_DESTROY)
            lifecycleOwner!!.lifecycle.removeObserver(lifecycleEventObserver!!)
        }
        lifecycleOwner = null
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = instantiateItem(position)
        val segment = retrieveSegmentFromObject(item)
        segment.attach(container.context, LayoutInflater.from(container.context))
        val view = segment.createView(container)
        container.addView(view.view)
        segment.bindView(view)
        syncSegmentStateWithParent(segment)
        return item
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, item: Any) {
        super.setPrimaryItem(container, position, item)
        val segment = retrieveSegmentFromObject(item)
        if (primaryItem !== segment) {
            val oldPrimaryItem = primaryItem
            primaryItem = segment
            syncSegmentStateWithParent(oldPrimaryItem)
            syncSegmentStateWithParent(primaryItem)
        }
    }

    protected abstract fun retrieveSegmentFromObject(item: Any): Segment<*>
    private fun syncSegmentStateWithParent(item: Segment<*>?) {
        if (item == null) {
            return
        }
        when (parentState) {
            SegmentViewState.FRESH -> {
            }
            SegmentViewState.CREATE -> item.onCreate()
            SegmentViewState.START -> item.onStart()
            SegmentViewState.RESUME -> {
                if (primaryItem === item) item.onResume()
                else item.onPause()
            }
            SegmentViewState.PAUSE -> item.onPause()
            SegmentViewState.STOP -> item.onStop()
            SegmentViewState.DESTROY -> item.onDestroy()
        }
    }

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
        val view = segment.getBoundedView()!!.view
        destroyItem(item)
        container.removeView(view)
    }

    override fun isViewFromObject(view: View, item: Any): Boolean {
        val segment = retrieveSegmentFromObject(item)
        val boundedView = segment.getBoundedView()
        return boundedView != null && boundedView.view === view
    }

    abstract fun instantiateItem(position: Int): Any
    open fun destroyItem(item: Any) {
        val segment = retrieveSegmentFromObject(item)
        segment.onStop()
        segment.unBindView()
        if (primaryItem === segment) {
            primaryItem = null
        }
    }

    fun handleBackPressed(): Boolean {
        return if (primaryItem == null) {
            false
        } else primaryItem!!.handleBackPressed()
    }

    fun onActivityResult(code: Int, resultCode: Int, data: Intent?) {
        if (primaryItem != null) {
            primaryItem!!.onActivityResult(code, resultCode, data)
        }
    }

    open fun onConfigurationChanged(configuration: Configuration?) {
        if (primaryItem != null) {
            primaryItem!!.onConfigurationChanged(configuration)
        }
    }

    fun onRequestPermissionsResult(code: Int, permissions: Array<String>, grantResults: IntArray?) {
        if (primaryItem != null) primaryItem!!.onRequestPermissionsResult(code, permissions, grantResults)
    }

    protected open fun onCreate() {
        parentState = SegmentViewState.CREATE
    }

    protected open fun onStart() {
        parentState = SegmentViewState.START
    }

    protected fun onResume() {
        parentState = SegmentViewState.RESUME
        if (primaryItem != null) {
            primaryItem!!.onResume()
        }
    }

    protected open fun onPause() {
        parentState = SegmentViewState.PAUSE
    }

    protected open fun onStop() {
        parentState = SegmentViewState.STOP
    }

    protected open fun onDestroy() {
        parentState = SegmentViewState.DESTROY
    }
}