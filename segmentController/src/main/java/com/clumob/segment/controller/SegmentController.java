package com.clumob.segment.controller;

import android.os.Parcel;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


/**
 * Created by prashant.rathore on 05/07/18.
 */

public class SegmentController<VM> {

    protected final Storable args;
    private final VM viewModel;

    public SegmentController(@Nullable Storable args, @NonNull VM viewModel) {
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

    public void onStop() {

    }

    public void onDestroy() {

    }
}
