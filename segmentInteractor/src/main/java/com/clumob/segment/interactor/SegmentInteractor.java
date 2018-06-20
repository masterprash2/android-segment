package com.clumob.segment.interactor;

import android.os.Bundle;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentInteractor<T extends SegmentViewModel> {

    private CompositeDisposable compositeDisposable;
    private T segmentViewModel;


    public void supplyParams(Bundle params) {
        segmentViewModel.supplyParams(params);
    }

    protected  Bundle getParams() {
        return this.segmentViewModel.getParams();
    }

    public void bindSegmentViewModel(T segmentViewModel) {
        unBindSegmentViewModel();
        this.segmentViewModel = segmentViewModel;
        compositeDisposable = new CompositeDisposable();
    }

    public T getViewModel() {
        return segmentViewModel;
    }

    public void onCreate() {
        segmentViewModel.freezeParams();

    }


    public void willShow() {

    }

    public void restoreState(Bundle inBundle) {

    }

    public void resume() {

    }

    public void pause() {

    }

    public void saveState(Bundle outBundle) {

    }

    public void willHide() {

    }

    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void unBindSegmentViewModel() {
        this.segmentViewModel = null;
        if(this.compositeDisposable != null) {
            this.compositeDisposable.dispose();
        }

    }

    public void onDestroy() {
        compositeDisposable.dispose();
        compositeDisposable = null;
    }

    public boolean handleBackPressed() {
        return false;
    }
}
