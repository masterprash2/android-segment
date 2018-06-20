package com.clumob.segment.interactor;

import android.os.Bundle;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class SegmentViewModel {

    private Bundle params;
    private boolean isParamsFrozen;

    void supplyParams(Bundle params) {
        if(!isParamsFrozen) {
            this.params = params;
        }
    }

    void freezeParams() {
        this.isParamsFrozen = true;
    }

    Bundle getParams() {
        return params;
    }


}
