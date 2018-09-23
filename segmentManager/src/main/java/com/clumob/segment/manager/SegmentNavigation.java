package com.clumob.segment.manager;

import com.clumob.segment.controller.SegmentInfo;

import java.util.Deque;
import java.util.LinkedList;

public class SegmentNavigation {

    private final SegmentManager segmentManager;
    private Deque<SegmentInfo> backStack = new LinkedList<>();

    public SegmentNavigation(SegmentManager segmentManager) {
        this.segmentManager = segmentManager;
    }

    public void addToBackStack(SegmentInfo segmentInfo) {
        SegmentInfo segmentInfoOld = navigateToScreen(segmentInfo);
        if (segmentInfoOld != null) {
            backStack.add(segmentInfoOld);
        }
    }

    public SegmentInfo navigateToScreen(SegmentInfo<?, ?> segmentInfo) {
        return segmentManager.changeSegment(segmentInfo);
    }

    public boolean popBackStack() {
        boolean b = this.backStack.size() > 0;
        if (b) {
            SegmentInfo segmentInfo = backStack.pollLast();
            navigateToScreen(segmentInfo);
        }
        return b;
    }

    public void clearStack() {
        backStack.clear();
    }

    public int getBackStackSize() {
        return backStack.size();
    }


}
