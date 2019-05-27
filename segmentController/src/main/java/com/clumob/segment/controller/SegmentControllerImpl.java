package com.clumob.segment.controller;

public class SegmentControllerImpl<T> implements SegmentController<T> {

    private final Storable args;
    private final T viewData;

    public SegmentControllerImpl(Storable args, T viewData) {
        this.args = args;
        this.viewData = viewData;
    }

    @Override
    public T getViewData() {
        return viewData;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void restoreState(Storable restorableState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
