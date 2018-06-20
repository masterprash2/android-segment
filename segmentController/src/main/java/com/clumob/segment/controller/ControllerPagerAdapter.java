package com.clumob.segment.controller;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.screen.SegmentView;

/**
 * Created by prashant.rathore on 26/02/18.
 */

public abstract class ControllerPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private SegmentController primaryItem;

    public ControllerPagerAdapter(Context context, LayoutInflater layoutInflater) {
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SegmentController segmentController = instantiateScreen(position);
        segmentController.attach(context, layoutInflater);
        segmentController.onCreate();
        SegmentView view = segmentController.createView(container);
        container.addView(view.getView());
        segmentController.bindView(view);
        segmentController.onStart();
        return segmentController;
    }

    protected abstract SegmentController instantiateScreen(int position);

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (primaryItem != null && primaryItem != object) {
            primaryItem.onPause();
        }
        if(primaryItem != object) {
            SegmentController segmentController = (SegmentController) object;
            primaryItem = segmentController;
            this.primaryItem.onResume();
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        SegmentController segmentController = (SegmentController) object;
        segmentController.onStop();
        SegmentView boundedView = segmentController.getBoundedView();
        container.removeView(boundedView.getView());
        segmentController.unBindView();
        segmentController.onDestroy();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        SegmentController segmentController = (SegmentController) object;
        SegmentView boundedView = segmentController.getBoundedView();
        return boundedView != null && boundedView.getView() == view;
    }
}
