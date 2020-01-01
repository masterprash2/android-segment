package com.clumob.segment.manager

import com.clumob.segment.controller.SegmentInfo
import java.util.*

open class SegmentNavigation(val segmentManager: SegmentManager) {
    private val backStack: Deque<SegmentInfo> = LinkedList()
    open fun addToBackStack(segmentInfo: SegmentInfo) {
        val segmentInfoOld = navigateToScreen(segmentInfo)
        if (segmentInfoOld != null) {
            backStack.add(segmentInfoOld)
        }
    }

    open fun navigateToScreen(segmentInfo: SegmentInfo): SegmentInfo? {
        return segmentManager.changeSegment(segmentInfo)
    }

    open fun popBackStack(): Boolean {
        val b = backStack.size > 0
        if (b) {
            val segmentInfo = backStack.pollLast()
            navigateToScreen(segmentInfo)
        }
        return b
    }

    open fun clearStack() {
        backStack.clear()
    }

    open val backStackSize: Int
        get() = backStack.size

}