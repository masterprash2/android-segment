package com.clumob.segment.interactor;

import android.os.Bundle;


/**
 * Created by prashant.rathore on 20/06/18.
 */

public class SegmentViewModel {

    private boolean isParamsFrozen;

    void supplyParams(Bundle params) {
        if (!isParamsFrozen) {
            processParams(params);
        }
    }

    protected void processParams(Bundle params) {

    }

    void freezeParams() {
        this.isParamsFrozen = true;
    }


}
