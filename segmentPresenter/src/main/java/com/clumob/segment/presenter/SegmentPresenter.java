package com.clumob.segment.presenter;

/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentPresenter<Args extends Storable, RestorableState extends Storable> {

    protected final Args args;

    public SegmentPresenter(Args args) {
        this.args = args;
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
        return null;
    }

    public void willHide() {

    }

    public void onDestroy() {

    }

    public boolean handleBackPressed() {
        return false;
    }
}
