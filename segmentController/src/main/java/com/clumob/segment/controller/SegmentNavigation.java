package com.clumob.segment.controller;

import com.clumob.segment.presenter.SegmentInfo;

import java.util.Deque;
import java.util.LinkedList;

public abstract class SegmentNavigation<T extends SegmentInfo> {

    private Deque<T> backStack = new LinkedList<>();

    public void addToBackStack(T segmentInfo) {
        T segmentInfoOld = navigateToScreen(segmentInfo);
        if(segmentInfoOld != null) {
            backStack.add(segmentInfoOld);
        }
    }

    public abstract T navigateToScreen(SegmentInfo segmentInfo);


    public boolean popBackStack() {
        boolean b = this.backStack.size() > 0;
        if (b) {
            T segmentInfo = backStack.pollLast();
            navigateToScreen(segmentInfo);
        }
        return b;
    }

    public void clearStack() {
        backStack.clear();
    }


}
