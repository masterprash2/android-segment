package com.clumob.segment.pager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.screen.SegmentView;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public abstract class SegmentPagerAdapter extends PagerAdapter {

    private SegmentController<?> primaryItem;

    @NonNull
    @Override
    final public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SegmentController<?> segmentController = instantiateItem(position);
        segmentController.attach(container.getContext(), LayoutInflater.from(container.getContext()));
        SegmentView view = segmentController.createView(container);
        container.addView(view.getView());
        segmentController.bindView(view);
        segmentController.onStart();
        return segmentController;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (primaryItem != object) {
            SegmentController newPrimaryItem = (SegmentController) object;
            if (this.primaryItem != null) {
                this.primaryItem.onPause();
            }
            newPrimaryItem.onResume();
            this.primaryItem = newPrimaryItem;
        }
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        SegmentController<?> segmentController = (SegmentController<?>) object;
        View view = segmentController.getBoundedView().getView();
        destroyItem(segmentController);
        container.removeView(view);
    }

    public abstract SegmentController<?> instantiateItem(int index);

    public void destroyItem(SegmentController<?> segmentController) {
        segmentController.onStop();
        segmentController.unBindView();
    }
}
