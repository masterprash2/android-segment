package com.clumob.segment.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.segment.presenter.SegmentPresenter;
import com.clumob.segment.presenter.SegmentViewModel;
import com.clumob.segment.presenter.Storable;
import com.clumob.segment.screen.SegmentView;


/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentController<VM extends SegmentViewModel, SI extends SegmentPresenter> {



    enum ScreenState {
        FRESH,
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    private final VM viewModel;
    private final SI interactor;
    private final SegmentFactory screenFactory;

    private SegmentView<VM, SI> boundedView = null;
    private SegmentInfo segmentInfo;

    private Context context;
    private LayoutInflater layoutInflater;

    ScreenState currentState = ScreenState.FRESH;

    public SegmentController(SegmentInfo segmentInfo, VM viewModel, SI interactor, SegmentFactory screenFactory) {
        this.screenFactory = screenFactory;
        this.segmentInfo = segmentInfo;
        this.viewModel = viewModel;
        this.interactor = interactor;
    }

    void attach(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    SegmentInfo getSegmentInfo() {
        return segmentInfo;
    }

    public SegmentView createView(ViewGroup parentView) {
        return screenFactory.create(context, layoutInflater, parentView);
    }

    public SegmentView getBoundedView() {
        return boundedView;
    }


    public void onCreate() {
        currentState = ScreenState.CREATE;
        interactor.onCreate();
        interactor.restoreState(segmentInfo.getRestorableModelState());
    }

    public void bindView(SegmentView<VM, SI> view) {
        boundedView = view;
        boundedView.bind(viewModel, interactor);
        boundedView.restoreState(segmentInfo.getSavedViewState());
    }

    public void onStart() {
        currentState = ScreenState.START;
        interactor.willShow();
        boundedView.willShow();
    }

    public void onResume() {
        currentState = ScreenState.RESUME;
        boundedView.resume();
        interactor.onResume();
    }

    public void onPause() {
        currentState = ScreenState.PAUSE;
        interactor.onPause();
        boundedView.pause();
        Bundle viewState = new Bundle();
        Storable stateSnapshot = interactor.createStateSnapshot();
        segmentInfo.setRestorableModelState(stateSnapshot);
        boundedView.saveState(viewState);
        segmentInfo.setSavedViewState(viewState);
    }

    public void onStop() {
        currentState = ScreenState.STOP;
        boundedView.willHide();
        interactor.willHide();
    }

    public void unBindView() {
        boundedView.unBind();
        boundedView = null;
    }

    public void onDestroy() {
        currentState = ScreenState.DESTROY;
        interactor.onDestroy();
    }

    void dettach() {
        this.context = null;
        this.layoutInflater = null;
    }

    public boolean handleBackPressed() {
        return interactor.handleBackPressed();
    }


}
