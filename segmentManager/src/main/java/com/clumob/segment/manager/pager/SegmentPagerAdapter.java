package com.clumob.segment.manager.pager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.manager.SegmentManager;
import com.clumob.segment.view.SegmentViewHolder;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public abstract class SegmentPagerAdapter extends PagerAdapter {

    private SegmentManager<?,?,?> primaryItem;

    @NonNull
    @Override
    final public Object instantiateItem(@NonNull ViewGroup container, int position) {
        SegmentManager<?,?,?> segmentManager = instantiateItem(position);
        segmentManager.attach(container.getContext(), LayoutInflater.from(container.getContext()));
        SegmentViewHolder view = segmentManager.createView(container);
        container.addView(view.getView());
        segmentManager.bindView(view);
        segmentManager.onStart();
        return segmentManager;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (primaryItem != object) {
            SegmentManager newPrimaryItem = (SegmentManager) object;
            if (this.primaryItem != null) {
                this.primaryItem.onPause();
            }
            newPrimaryItem.onResume();
            this.primaryItem = newPrimaryItem;
        }
    }

    @Override
    final public int getItemPosition(@NonNull Object object) {
        return computeItemPosition((SegmentManager<?,?,?>) object);
    }

    public int computeItemPosition(SegmentManager<?,?,?> segmentManager) {
        return POSITION_UNCHANGED;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        SegmentManager<?,?,?> segmentManager = (SegmentManager<?,?,?>) object;
        View view = segmentManager.getBoundedView().getView();
        destroyItem(segmentManager);
        container.removeView(view);
    }

    public abstract SegmentManager<?,?,?> instantiateItem(int position);

    public void destroyItem(SegmentManager<?,?,?> segmentManager) {
        segmentManager.onStop();
        segmentManager.unBindView();
    }
}
