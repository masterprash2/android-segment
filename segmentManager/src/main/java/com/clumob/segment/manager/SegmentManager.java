package com.clumob.segment.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentPresenter;
import com.clumob.segment.controller.Storable;
import com.clumob.segment.screen.SegmentView;


/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentController<Presenter extends SegmentPresenter<Storable, Storable>> {

    enum ScreenState {
        FRESH,
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    private final Presenter presenter;
    private final SegmentFactory screenFactory;

    private SegmentView<Presenter> boundedView = null;
    private SegmentInfo segmentInfo;

    private Context context;
    private LayoutInflater layoutInflater;

    ScreenState currentState = ScreenState.FRESH;

    public SegmentController(SegmentInfo segmentInfo, Presenter presenter, SegmentFactory screenFactory) {
        this.screenFactory = screenFactory;
        this.segmentInfo = segmentInfo;
        this.presenter = presenter;
    }

    public void attach(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    public SegmentInfo getSegmentInfo() {
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
        presenter.onCreate();
        presenter.restoreState(segmentInfo.getRestorableModelState());
    }

    public void bindView(SegmentView<Presenter> view) {
        boundedView = view;
        boundedView.bind(presenter);
//        boundedView.restoreState(segmentInfo.getSavedViewState());
    }

    public void onStart() {
        currentState = ScreenState.START;
        presenter.willShow();
        boundedView.willShow();
    }

    public void onResume() {
        currentState = ScreenState.RESUME;
        boundedView.resume();
        presenter.onResume();
    }

    public void onPause() {
        currentState = ScreenState.PAUSE;
        presenter.onPause();
        boundedView.pause();
//        Bundle viewState = new Bundle();
        Storable stateSnapshot = presenter.createStateSnapshot();
        segmentInfo.setRestorableModelState(stateSnapshot);
//        boundedView.saveState(viewState);
//        segmentInfo.setSavedViewState(viewState);
    }

    public void onStop() {
        currentState = ScreenState.STOP;
        boundedView.willHide();
        presenter.willHide();
    }

    public void unBindView() {
        boundedView.unBind();
        boundedView = null;
    }

    public void onDestroy() {
        currentState = ScreenState.DESTROY;
        presenter.onDestroy();
    }

    void dettach() {
        this.context = null;
        this.layoutInflater = null;
    }

    public boolean handleBackPressed() {
        return presenter.handleBackPressed();
    }


}
