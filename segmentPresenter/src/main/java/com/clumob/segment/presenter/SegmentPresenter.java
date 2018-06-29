package com.clumob.segment.presenter;

/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentPresenter<Args extends Storable, RestorableState extends Storable, SVM extends SegmentViewModel<Args, RestorableState>> {

    protected final Args args;
    protected final SVM viewModel;

    public SegmentPresenter(Args args, SVM viewModel) {
        this.args = args;
        this.viewModel = viewModel;
    }

    public void onCreate() {

    }

    public void restoreState(RestorableState restorableState) {
    }

    public void willShow() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public RestorableState createStateSnapshot() {
        return viewModel.createSnapshot();
    }

    public void willHide() {

    }

    public void onDestroy() {

    }

    public boolean handleBackPressed() {
        return false;
    }
}
