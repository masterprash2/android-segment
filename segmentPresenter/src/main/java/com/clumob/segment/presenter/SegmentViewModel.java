package com.clumob.segment.presenter;


/**
 * Created by prashant.rathore on 20/06/18.
 */

public class SegmentViewModel<Args extends Storable, RestorableState extends Storable> {

    public SegmentViewModel() {
    }


    public RestorableState createSnapshot() {
        return null;
    }
}
