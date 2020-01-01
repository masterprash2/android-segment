package com.clumob.segment.manager

import com.clumob.segment.controller.SegmentInfo
import java.util.*

open class SegmentNavigation(private val segmentManager: SegmentManager) {
    private val backStack: Deque<SegmentInfo> = LinkedList()
    fun addToBackStack(segmentInfo: SegmentInfo) {
        val segmentInfoOld = navigateToScreen(segmentInfo)
        if (segmentInfoOld != null) {
            backStack.add(segmentInfoOld)
        }
    }

    fun navigateToScreen(segmentInfo: SegmentInfo): SegmentInfo? {
        return segmentManager.changeSegment(segmentInfo)
    }

    fun popBackStack(): Boolean {
        val b = backStack.size > 0
        if (b) {
            val segmentInfo = backStack.pollLast()
            navigateToScreen(segmentInfo)
        }
        return b
    }

    fun clearStack() {
        backStack.clear()
    }

    val backStackSize: Int
        get() = backStack.size

}