package com.clumob.segment.support.pager.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import java.util.*

/**
 * Created by prashant.rathore on 11/07/18.
 */
class SegmentPageChangeHelper(private val snapHelper: SnapHelper) {
    var currentSegmentItemViewHolder: SegmentItemViewHolder<*, *>? = null
        private set
    private var currentPageIndex = -1
    private var nextPageIndex = 0
    private val pageChangeListner: PageChangeListner = createPageChangeListener()
    private val pageChangeListners: MutableList<PageChangeListner> = LinkedList()
    private var attachedRecyclerView: RecyclerView? = null
    private fun createPageChangeListener(): PageChangeListner {
        return object : PageChangeListner {
            override fun onPageChanged(position: Int) {
                for (listner in pageChangeListners) {
                    listner.onPageChanged(position)
                }
            }

            override fun onPageScrolled(position: Int, offset: Float) {
                for (listner in pageChangeListners) {
                    listner.onPageScrolled(position, offset)
                }
            }
        }
    }

    fun addPageChangeListner(listner: PageChangeListner) {
        pageChangeListners.add(listner)
    }

    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (currentSegmentItemViewHolder == null) return
            var x: Float
            val adapterPosition = currentSegmentItemViewHolder!!.getAdapterPosition()
            val snapView: SegmentItemViewHolder<*, *> = currentSegmentItemViewHolder!!
            val itemView = snapView.getItemView()
            x = itemView.left.toFloat()
            if (x <= 0 || nextPageIndex >= 0) {
                if (nextPageIndex < 0) {
                    nextPageIndex = adapterPosition
                }
                pageChangeListner.onPageScrolled(nextPageIndex, -x * 1f / itemView.measuredWidth)
            } else {
                if (nextPageIndex < 0) {
                    nextPageIndex = adapterPosition - 1
                }
                x = itemView.measuredWidth - x
                pageChangeListner.onPageScrolled(nextPageIndex, x * 1f / itemView.measuredWidth)
            }
            nextPageIndex = -1
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val view = snapHelper.findSnapView(recyclerView.layoutManager) ?: return
                val snapView = recyclerView.getChildViewHolder(view) as SegmentItemViewHolder<*, *>
                val pageIndex = snapView.getAdapterPosition()
                if (pageIndex != currentPageIndex) {
                    if (currentSegmentItemViewHolder != null) {
                        currentSegmentItemViewHolder!!.pause()
                    }
                    snapView.resume()
                    currentPageIndex = snapView.getAdapterPosition()
                    currentSegmentItemViewHolder = snapView
                    pageChangeListner.onPageChanged(currentPageIndex)
                }
            }
        }
    }

    fun handleBackPressed(): Boolean {
        return currentSegmentItemViewHolder!!.handleBackPressed()
    }

    fun attachRecyclerView(recyclerView: RecyclerView) {
        attachedRecyclerView = recyclerView
        recyclerView.addOnScrollListener(listener)
        recyclerView.post { listener.onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE) }
    }

    fun scrollToPosition(position: Int) {
        if (currentPageIndex != position) {
            nextPageIndex = position
            attachedRecyclerView!!.scrollToPosition(position)
            attachedRecyclerView!!.post { listener.onScrollStateChanged(attachedRecyclerView!!, RecyclerView.SCROLL_STATE_IDLE) }
        }
    }

    interface PageChangeListner {
        fun onPageChanged(position: Int)
        fun onPageScrolled(position: Int, offset: Float)
    }

}