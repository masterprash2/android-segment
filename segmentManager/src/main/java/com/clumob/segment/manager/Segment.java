package com.clumob.segment.manager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentPresenter;
import com.clumob.segment.controller.Storable;


/**
 * Created by prashant.rathore on 02/02/18.
 */

public class Segment<VM, Presenter extends SegmentPresenter<VM>, Controller extends SegmentController<VM, Presenter>> {



    public enum SegmentState {
        FRESH,
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    private final Controller controller;
    private final SegmentFactory screenFactory;

    private SegmentViewHolder<VM, Controller> boundedView = null;
    private SegmentInfo<Storable, Storable> segmentInfo;

    private Context context;
    private LayoutInflater layoutInflater;


    SegmentState currentState = SegmentState.FRESH;

    public Segment(SegmentInfo segmentInfo, Controller controller, SegmentFactory screenFactory) {
        this.screenFactory = screenFactory;
        this.segmentInfo = segmentInfo;
        this.controller = controller;
    }

    public void attach(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    public SegmentInfo getSegmentInfo() {
        return segmentInfo;
    }

    public SegmentViewHolder createView(ViewGroup parentView) {
        return screenFactory.create(context, layoutInflater, parentView);
    }

    public SegmentViewHolder getBoundedView() {
        return boundedView;
    }


    public void onCreate() {
        currentState = SegmentState.CREATE;
        controller.onCreate();
        controller.restoreState(segmentInfo.getRestorableSetmentState());
    }

    public void bindView(SegmentViewHolder<VM, Controller> viewHolder) {
        boundedView = viewHolder;
        boundedView.bind(this, controller.getViewModel(), controller);
//        boundedView.restoreState(segmentInfo.getSavedViewState());
    }

    public void onStart() {
        currentState = SegmentState.START;
        controller.willShow();
        boundedView.willShow();
    }

    public void onResume() {
        currentState = SegmentState.RESUME;
        boundedView.resume();
        controller.onResume();
    }

    public void onPause() {
        currentState = SegmentState.PAUSE;
        controller.onPause();
        boundedView.pause();
        final Storable viewState = boundedView.createStateSnapshot();
        final Storable stateSnapshot = controller.createStateSnapshot(viewState);
        segmentInfo.setRestorableSegmentState(stateSnapshot);
    }

    public void onStop() {
        currentState = SegmentState.STOP;
        boundedView.willHide();
        controller.willHide();
    }

    public void unBindView() {
        boundedView.unBind();
        boundedView = null;
    }

    public void onDestroy() {
        currentState = SegmentState.DESTROY;
        controller.onDestroy();
    }

    void dettach() {
        this.context = null;
        this.layoutInflater = null;
    }

    public void onActivityResult(int code, int resultCode, Intent data) {
        boundedView.onActivityResult(code,resultCode,data);
    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] grantResults) {
        boundedView.onRequestPermissionsResult(code, permissions, grantResults);
    }

    public boolean handleBackPressed() {
        return boundedView.handleBackPressed();
    }

}
