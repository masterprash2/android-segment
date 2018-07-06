package com.clumob.segment.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentPresenter;
import com.clumob.segment.controller.Storable;
import com.clumob.segment.view.SegmentViewHolder;


/**
 * Created by prashant.rathore on 02/02/18.
 */

public class Segment<VM, Presenter extends SegmentPresenter<VM>, Controller extends SegmentController<VM, Presenter>> {

    enum ScreenState {
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
    private SegmentInfo<Storable,Storable> segmentInfo;

    private Context context;
    private LayoutInflater layoutInflater;

    ScreenState currentState = ScreenState.FRESH;

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
        currentState = ScreenState.CREATE;
        controller.onCreate();
        controller.restoreState(segmentInfo.getRestorableSetmentState());
    }

    public void bindView(SegmentViewHolder<VM, Controller> viewHolder) {
        boundedView = viewHolder;
        boundedView.bind(controller.getViewModel(), controller);
//        boundedView.restoreState(segmentInfo.getSavedViewState());
    }

    public void onStart() {
        currentState = ScreenState.START;
        controller.willShow();
        boundedView.willShow();
    }

    public void onResume() {
        currentState = ScreenState.RESUME;
        boundedView.resume();
        controller.onResume();
    }

    public void onPause() {
        currentState = ScreenState.PAUSE;
        controller.onPause();
        boundedView.pause();
        final Storable viewState = boundedView.createStateSnapshot();
        final Storable stateSnapshot = controller.createStateSnapshot(viewState);
        segmentInfo.setRestorableSegmentState(stateSnapshot);
    }

    public void onStop() {
        currentState = ScreenState.STOP;
        boundedView.willHide();
        controller.willHide();
    }

    public void unBindView() {
        boundedView.unBind();
        boundedView = null;
    }

    public void onDestroy() {
        currentState = ScreenState.DESTROY;
        controller.onDestroy();
    }

    void dettach() {
        this.context = null;
        this.layoutInflater = null;
    }

    public boolean handleBackPressed() {
        return controller.handleBackPressed();
    }


}
