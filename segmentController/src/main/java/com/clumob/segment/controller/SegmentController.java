package com.clumob.segment.controller;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;


/**
 * Created by prashant.rathore on 05/07/18.
 */
public interface SegmentController<VD> {

    public VD getViewData();

    public void onCreate();

    public void restoreState(Storable restorableState);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();
}
