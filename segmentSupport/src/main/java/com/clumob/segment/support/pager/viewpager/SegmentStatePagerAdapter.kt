package com.clumob.segment.support.pager.viewpager

import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import androidx.viewpager.widget.PagerAdapter
import com.clumob.listitem.controller.source.ItemController
import com.clumob.listitem.controller.source.ItemControllerSource
import com.clumob.listitem.controller.source.SourceUpdateEvent
import com.clumob.segment.controller.list.SegmentItemController
import com.clumob.segment.manager.Segment
import com.clumob.segment.support.pager.SegmentItemProvider
import io.reactivex.observers.DisposableObserver
import java.util.*

open class SegmentStatePagerAdapter<T : SegmentItemController>(val dataSource : ItemControllerSource<T>,
                                    val factory: SegmentItemProvider) : SegmentPagerAdapter() {

    private val attachedSegments: MutableSet<ItemSegmentPair> = HashSet()
    private val mHandler = Handler(Looper.getMainLooper())

    init {
        dataSource.viewInteractor = (createViewInteractor())
        dataSource.observeAdapterUpdates().subscribe(object : DisposableObserver<SourceUpdateEvent?>() {
            override fun onNext(sourceUpdateEvent: SourceUpdateEvent) {
                if (sourceUpdateEvent.type == SourceUpdateEvent.Type.UPDATE_ENDS) notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onComplete() {}
        })
    }

    private fun createViewInteractor() : ItemControllerSource.ViewInteractor {
        return object : ItemControllerSource.ViewInteractor {
            var deque: Deque<Runnable> = LinkedList()
            private var processingInProgress = false
            override fun processWhenSafe(runnable: Runnable) {
                deque.add(runnable)
                if (!processingInProgress) {
                    processingInProgress = true
                    this.processWhenQueueIdle()
                }
            }

            private fun processWhenQueueIdle() {
                mHandler.post(object : Runnable {
                    override fun run() {
                        if (isComputingLayout()) {
                            mHandler.post(this);
                        } else if (deque.peekFirst() != null) {
                            val runnable = deque.pollFirst();
                            runnable.run();
                            mHandler.post(this);
                        } else {
                            processingInProgress = false;
                        }
                    }
                });
            }

            override fun cancelOldProcess(runnable: Runnable) {
                mHandler.removeCallbacks(runnable)
            }
        };
    }

    private fun isComputingLayout(): Boolean {
        return false
    }

    class ItemSegmentPair(val segment: Segment, val itemController: ItemController?) {

        companion object {
            fun pair(segment: Segment, itemController: ItemController?): ItemSegmentPair {
                return ItemSegmentPair(segment, itemController)
            }
        }

    }

    override fun instantiateItem(index: Int): Any {
        val item = getItem(index)
        val segment = factory.provide(item)
        val pair = ItemSegmentPair.pair(segment, item)
        attachedSegments.add(pair)
        segment.onCreate()
        dataSource.onItemAttached(index)
        pair.itemController!!.onAttach(pair.segment)
        return pair
    }

    override fun getCount(): Int {
        return dataSource.itemCount
    }

    fun getAttachedSegments(): Set<ItemSegmentPair?>? {
        return attachedSegments
    }

    override fun retrieveSegmentFromObject(item: Any): Segment {
        return (item as ItemSegmentPair).segment
    }

    // ToDO: The lookup algorightm is slow;
    override fun computeItemPosition(inputItem: Any): Int {
        val id = (inputItem as ItemSegmentPair).itemController!!.id
        val itemCount = dataSource.itemCount
        for (i in 0 until itemCount) {
            val item = getItem(i)
            if (item.id == id) {
                return i
            }
        }
        return PagerAdapter.POSITION_NONE
    }


    override fun destroyItem(item: Any) {
        super.destroyItem(item)
        val pair = item as ItemSegmentPair
        pair.itemController!!.onDetach(pair.segment)
        pair.segment.onDestroy()
        attachedSegments.remove(pair)
    }


    override fun onCreate() {
        super.onCreate()
        for (segment in attachedSegments) {
            segment.segment.onCreate()
        }
    }

    override fun onStart() {
        super.onStart()
        for (segment in attachedSegments) {
            segment.segment.onStart()
        }
    }

    override fun onPause() {
        super.onPause()
        for (segment in attachedSegments) {
            segment.segment.onPause()
        }
    }

    override fun onConfigurationChanged(configuration: Configuration?) {
        for (segment in attachedSegments) {
            segment.segment.onConfigurationChanged(configuration)
        }
    }

    override fun onStop() {
        super.onStop()
        for (segment in attachedSegments) {
            segment.segment.onStop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        for (segment in attachedSegments) {
            segment.segment.onDestroy()
        }
    }

    fun getItem(position: Int): T {
        return dataSource.getItem(position)
    }

}
