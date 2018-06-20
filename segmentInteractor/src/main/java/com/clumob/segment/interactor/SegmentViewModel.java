package com.clumob.segment.interactor;

import android.os.Bundle;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class SegmentViewModel {

    private CompositeDisposable compositeDisposable;
    private boolean isParamsFrozen;

    void prepare() {
        this.compositeDisposable = new CompositeDisposable();
    }

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

    public void addDisposable(Disposable disposableObserver) {
        if (this.compositeDisposable == null) {
            throw new NullPointerException("Prepare should be invoked after before initial Use and after Flush");
        }
        this.compositeDisposable.add(disposableObserver);
    }

    void flush() {
        this.compositeDisposable.dispose();
        this.compositeDisposable = null;
    }


}
