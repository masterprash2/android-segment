package com.clumob.segment.controller;

import android.os.Parcel;

/**
 * Created by prashant.rathore on 05/07/18.
 */

public class SegmentController<VM> {

    protected final Storable args;
    private final VM viewModel;

    public SegmentController(Storable args, VM viewModel) {
        this.args = args;
        this.viewModel = viewModel;
    }

    public VM getViewModel() {
        return viewModel;
    }

    public void onCreate() {

    }

    public void restoreState(Storable restorableState) {

    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public Storable createStateSnapshot(Storable viewState) {
        return null;
    }

    public void onStop() {

    }

    public void onDestroy() {

    }
}
