package com.clumob.segment.controller;

import com.clumob.list.presenter.source.ViewModelPresenter;

/**
 * Created by prashant.rathore on 03/07/18.
 */

public class SegmentPagerItemPresenter extends ViewModelPresenter<SegmentInfo> {

    public SegmentPagerItemPresenter(SegmentInfo viewModel) {
        super(viewModel);
    }

    @Override
    final public void onCreate() {
        super.onCreate();
    }

    @Override
    final public void onAttach() {
        super.onAttach();
    }

    @Override
    final public void onDetach() {
        super.onDetach();
    }

    @Override
    final public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    final public long getId() {
        return viewModel.getId();
    }
}
