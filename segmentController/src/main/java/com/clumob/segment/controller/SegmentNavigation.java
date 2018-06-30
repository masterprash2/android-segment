package com.clumob.segment.controller;

import com.clumob.segment.presenter.SegmentInfo;

import java.util.Deque;
import java.util.LinkedList;

public abstract class SegmentNavigation {

    private Deque<SegmentInfo> backStack = new LinkedList<>();

    public void addToBackStack(SegmentInfo segmentInfo) {
        SegmentInfo segmentInfoOld = navigateToScreen(segmentInfo);
        if(segmentInfoOld != null) {
            backStack.add(segmentInfoOld);
        }
    }

    public abstract SegmentInfo navigateToScreen(SegmentInfo segmentInfo);


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


}
