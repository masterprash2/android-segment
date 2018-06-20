package com.clumob.segment.interactor;

import android.os.Bundle;


/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentInteractor<T extends SegmentViewModel> {

    public void supplyParams(T viewModel, Bundle params) {
        viewModel.supplyParams(params);
    }

    public void onCreate(T viewModel) {
        viewModel.freezeParams();
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

    public void onDestroy(T viewModel) {
    }

    public boolean handleBackPressed(T viewModel) {
        return false;
    }
}
