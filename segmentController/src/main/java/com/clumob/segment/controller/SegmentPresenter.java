package com.clumob.segment.controller;

/**
 * Created by prashant.rathore on 02/02/18.
 */
public class SegmentPresenter<T> {

    private T viewModel;

    public SegmentPresenter(T viewModel) {
        this.viewModel = viewModel;
    }

    T getViewModel() {
        return viewModel;
    }
}
