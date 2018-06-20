package com.clumob.segment.interactor;

import android.os.Bundle;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentInteractor<T extends SegmentViewModel> {


    public void supplyParams(T viewModel, Bundle params) {
        viewModel.supplyParams(params);
    }

    public void onCreate(T viewModel) {
        viewModel.freezeParams();
        viewModel.prepare();
    }


    public void willShow(T viewModel) {

    }

    public void restoreState(T viewModel, Bundle inBundle) {

    }

    public void onResume(T viewModel) {

    }

    public void onPause(T viewModel) {

    }

    public void saveState(T viewModel, Bundle outBundle) {

    }

    public void willHide(T viewModel) {

    }

    protected void addDisposable(T viewModel, Disposable disposable) {
        viewModel.addDisposable(disposable);
    }

    public void onDestroy(T viewModel) {
        viewModel.flush();
    }

    public boolean handleBackPressed(T viewModel) {
        return false;
    }
}
