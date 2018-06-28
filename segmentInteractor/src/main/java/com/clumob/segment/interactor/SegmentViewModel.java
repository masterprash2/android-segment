package com.clumob.segment.interactor;

import android.os.Parcelable;


/**
 * Created by prashant.rathore on 20/06/18.
 */

public class SegmentViewModel<Args extends Storable, RestorableState extends Storable> {

    private Args arguments;

    public SegmentViewModel(Args arguments) {
        this.arguments = arguments;
    }

    public void restoreState(RestorableState restorableState) {

    }

    public RestorableState createSnapshot() {
        return null;
    }
}
