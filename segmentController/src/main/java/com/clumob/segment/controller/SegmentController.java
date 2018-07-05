package com.clumob.segment.controller;

import android.os.Parcel;

/**
 * Created by prashant.rathore on 05/07/18.
 */

public class SegmentController<VM, Presenter extends SegmentPresenter<VM>> {

    protected final Storable args;
    protected final Presenter presenter;

    public SegmentController(Storable args, Presenter presenter) {
        this.args = args;
        this.presenter = presenter;
    }

    public VM getViewModel() {
        return presenter.getViewModel();
    }

    public void onCreate() {

    }

    public void restoreState(Storable restorableState) {

    }

    public void willShow() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public Storable createStateSnapshot(Storable viewState) {
        return null;
    }

    public void willHide() {

    }

    public void onDestroy() {

    }

    public boolean handleBackPressed() {
        return false;
    }
}
