package com.clumob.segment.manager;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.log.AppLog;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.Storable;


/**
 * Created by prashant.rathore on 02/02/18.
 */

public class Segment<VM, Controller extends SegmentController<VM>> implements LifecycleOwner {


    LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }

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
    private final SegmentViewHolderFactory screenFactory;

    private SegmentViewHolder<VM, Controller> boundedView = null;
    private SegmentInfo<Storable, Storable> segmentInfo;

    private Context context;
    private LayoutInflater layoutInflater;

    SegmentState currentState = SegmentState.FRESH;

    public Segment(SegmentInfo segmentInfo, Controller controller, SegmentViewHolderFactory screenFactory) {
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
        SegmentViewHolder<?, ?> segmentViewHolder = screenFactory.create(context, layoutInflater, parentView);
        return segmentViewHolder;
    }

    public SegmentViewHolder getBoundedView() {
        return boundedView;
    }

    public void onCreate() {
        switch (this.currentState) {
            case FRESH:
            case DESTROY:
                createInternal();
                break;
        }
    }

    private void createInternal() {
        currentState = SegmentState.CREATE;
        controller.onCreate();
        controller.restoreState(segmentInfo.getRestorableSetmentState());
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    public void bindView(SegmentViewHolder<VM, Controller> viewHolder) {
        boundedView = viewHolder;
        boundedView.attachLifecycleOwner(this);
        boundedView.bind(this, controller.getViewData(), controller);
//        boundedView.restoreState(segmentInfo.getSavedViewState());
    }

    public void onConfigurationChanged(Configuration newConfig) {
        boundedView.onConfigurationChanged(newConfig);
    }

    public void onStart() {
        switch (currentState) {
            case DESTROY:
            case FRESH:
            case CREATE:
                onCreate();
            case STOP:
                startInternal();
            case START:
                break;
            case RESUME:
                break;
            case PAUSE:
                break;
        }
    }

    private void startInternal() {
        currentState = SegmentState.START;
        controller.onStart();
        boundedView.onStart();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    public void onResume() {
        if (currentState != SegmentState.RESUME) {
            onStart();
            resumeInternal();
        }
    }

    private void resumeInternal() {
        currentState = SegmentState.RESUME;
        boundedView.resume();
        controller.onResume();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    public void onPause() {
        switch (currentState) {
            case FRESH:
            case CREATE:
                onStart();
            case RESUME:
                pauseInternal();
                break;
            case START:
            case PAUSE:
            case STOP:
            case DESTROY:
                break;
        }
    }

    private void pauseInternal() {
        currentState = SegmentState.PAUSE;
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        controller.onPause();
        boundedView.pause();
        final Storable viewState = boundedView.createStateSnapshot();
        segmentInfo.setRestorableSegmentState(viewState);

    }

    public void onStop() {
        switch (this.currentState) {
            case DESTROY:
            case FRESH:
                onCreate();
                break;
            case RESUME:
                onPause();
            case PAUSE:
            case START:
                stopInternal();
                break;
        }
    }


    private void stopInternal() {
        currentState = SegmentState.STOP;
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        boundedView.onStop();
        controller.onStop();

    }

    public void unBindView() {
        if (boundedView != null) {
            boundedView.detachLifecycleOwner();
            boundedView.unBind();
            boundedView = null;
        }
    }

    public void onDestroy() {
        if (this.currentState != SegmentState.DESTROY) {
            onStop();
            unBindView();
            destroyInternal();
        }
    }

    private void destroyInternal() {
        currentState = SegmentState.DESTROY;
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        controller.onDestroy();
    }

    void dettach() {
        this.context = null;
        this.layoutInflater = null;
    }

    public void onActivityResult(int code, int resultCode, Intent data) {
        boundedView.onActivityResult(code, resultCode, data);
    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] grantResults) {
        boundedView.onRequestPermissionsResult(code, permissions, grantResults);
    }

    public boolean handleBackPressed() {
        if(boundedView == null) {
            AppLog.d("SEGMENT","SegmentInfo " + segmentInfo);
            AppLog.printStack(new NullPointerException("Cannot handle backpressed SegmentView is Null"));
            return false;
        }
        else {
            return boundedView.handleBackPressed();
        }

    }

    public boolean isResumed() {
        return currentState == SegmentState.RESUME;
    }

}
