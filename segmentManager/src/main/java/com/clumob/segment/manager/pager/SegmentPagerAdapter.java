package com.clumob.segment.manager.pager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentViewHolder;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public abstract class SegmentPagerAdapter extends PagerAdapter {

    private Segment<?,?,?> primaryItem;


    @NonNull
    @Override
    final public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Segment<?,?,?> segment = instantiateItem(position);
        segment.attach(container.getContext(), LayoutInflater.from(container.getContext()));
        SegmentViewHolder view = segment.createView(container);
        container.addView(view.getView());
        segment.bindView(view);
        segment.onStart();
        return segment;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (primaryItem != object) {
            Segment newPrimaryItem = (Segment) object;
            if (this.primaryItem != null) {
                this.primaryItem.onPause();
            }
            newPrimaryItem.onResume();
            this.primaryItem = newPrimaryItem;
        }
    }

    @Override
    final public int getItemPosition(@NonNull Object object) {
        return computeItemPosition((Segment<?,?,?>) object);
    }

    public int computeItemPosition(Segment<?,?,?> segment) {
        return POSITION_UNCHANGED;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Segment<?,?,?> segment = (Segment<?,?,?>) object;
        View view = segment.getBoundedView().getView();
        destroyItem(segment);
        container.removeView(view);
    }

    public abstract Segment<?,?,?> instantiateItem(int position);

    public void destroyItem(Segment<?,?,?> segment) {
        segment.onStop();
        segment.unBindView();
    }

    public boolean handleBackPressed() {
        if(primaryItem == null) {
            return false;
        }
        return primaryItem.handleBackPressed();
    }
}
