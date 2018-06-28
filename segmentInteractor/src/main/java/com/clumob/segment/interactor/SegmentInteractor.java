package com.clumob.segment.interactor;

/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentInteractor<Args extends Storable, RestorableState extends Storable, SVM extends SegmentViewModel<Args, RestorableState>> {

    public void onCreate(SVM viewModel) {

    }

    public void restoreState(SVM viewModel, RestorableState restorableState) {
        viewModel.restoreState(restorableState);
    }

    public void willShow(SVM viewModel) {

    }

    public void onResume(SVM viewModel) {

    }

    public void onPause(SVM viewModel) {

    }

    public RestorableState createStateSnapshot(SVM viewModel) {
        return viewModel.createSnapshot();
    }

    public void willHide(SVM viewModel) {

    }

    public void onDestroy(SVM viewModel) {
    }

    public boolean handleBackPressed(SVM viewModel) {
        return false;
    }

}
