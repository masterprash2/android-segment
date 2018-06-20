package com.clumob.segment.controller;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by arpit.toshniwal on 10/03/18.
 */

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
