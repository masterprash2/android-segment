package com.clumob.segment.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.segment.interactor.SegmentViewModel;
import com.clumob.segment.screen.SegmentView;
import com.clumob.segment.interactor.SegmentInteractor;


/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentController<VM extends SegmentViewModel, SI extends SegmentInteractor> {


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

    private SegmentView<VM,SI> boundedView = null;
    private SegmentInfo segmentInfo;

    private static final String KEY_VIEW_STATE = "viewState";
    private static final String KEY_VIEWMODEL_STATE = "viewModelState";
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

    public SegmentView<VM,SI> createView(ViewGroup parentView) {
        return (SegmentView<VM,SI>) screenFactory.create(context, layoutInflater, parentView);
    }

    public SegmentView getBoundedView() {
        return boundedView;
    }


    public void onCreate() {
        currentState = ScreenState.CREATE;
        interactor.supplyParams(viewModel,segmentInfo.getParams());
        interactor.onCreate(viewModel);
        Bundle savedState = segmentInfo.getSavedState();
        interactor.restoreState(viewModel,savedState.getBundle(KEY_VIEWMODEL_STATE));
    }

    public void bindView(SegmentView<VM,SI> view) {
        boundedView = view;
        boundedView.bind(viewModel,interactor);
        Bundle savedState = segmentInfo.getSavedState();
        boundedView.restoreState(savedState.getBundle(KEY_VIEW_STATE));
    }

    public void onStart() {
        currentState = ScreenState.START;
        interactor.willShow(viewModel);
        boundedView.willShow();
    }

    public void onResume() {
        currentState = ScreenState.RESUME;
        boundedView.resume();
        interactor.onResume(viewModel);
    }

    public void onPause() {
        currentState = ScreenState.PAUSE;
        interactor.onPause(viewModel);
        boundedView.pause();
        Bundle viewState = new Bundle();
        Bundle viewModleState = new Bundle();

        interactor.saveState(viewModel,viewModleState);
        boundedView.saveState(viewState);

        segmentInfo.getSavedState().putBundle(KEY_VIEWMODEL_STATE, viewModleState);
        segmentInfo.getSavedState().putBundle(KEY_VIEW_STATE, viewState);
    }

    public void onStop() {
        currentState = ScreenState.STOP;
        boundedView.willHide();
        interactor.willHide(viewModel);
    }

    public void unBindView() {
        boundedView.unBind();
        boundedView = null;
    }

    public void onDestroy() {
        currentState = ScreenState.DESTROY;
        interactor.onDestroy(viewModel);
    }

    void dettach() {
        this.context = null;
        this.layoutInflater = null;
    }

    public boolean handleBackPressed() {
        return interactor.handleBackPressed(viewModel);
    }

}
