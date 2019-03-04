package com.clumob.segment.support.pager.viewpager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentLifecycle;
import com.clumob.segment.manager.SegmentViewHolder;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public abstract class SegmentPagerAdapter extends PagerAdapter implements SegmentLifecycle {

    private Segment<?, ?> primaryItem;
    private SegmentViewHolder.SegmentViewState parentState = SegmentViewHolder.SegmentViewState.FRESH;

    @NonNull
    @Override
    final public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object item = instantiateItem(position);
        Segment<?, ?> segment = retrieveSegmentFromObject(item);
        segment.attach(container.getContext(), LayoutInflater.from(container.getContext()));
        SegmentViewHolder view = segment.createView(container);
        container.addView(view.getView());
        segment.bindView(view);
        syncSegmentStateWithParent(segment);
        return item;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        Segment<?, ?> segment = retrieveSegmentFromObject(object);
        if (primaryItem != segment) {
            final Segment oldPrimaryItem = this.primaryItem;
            this.primaryItem = segment;
            syncSegmentStateWithParent(oldPrimaryItem);
            syncSegmentStateWithParent(this.primaryItem);
        }
    }

    protected abstract Segment<?,?> retrieveSegmentFromObject(Object object);


    private void syncSegmentStateWithParent(Segment item) {
        if(item == null) {
            return;
        }
        switch (parentState) {
            case FRESH:
                break;
            case CREATE:
                item.onCreate();
                break;
            case START:
                item.onStart();
                break;
            case RESUME:
                if(this.primaryItem == item) {
                    item.onResume();
                    break;
                }
            case PAUSE:
                item.onPause();
                break;
            case STOP:
                item.onStop();
                break;
            case DESTROY:
                item.onDestroy();
                break;
        }
    }


    @Override
    final public int getItemPosition(@NonNull Object object) {
        return computeItemPosition(object);
    }

    public int computeItemPosition(Object item) {
        return POSITION_UNCHANGED;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Segment<?, ?> segment = retrieveSegmentFromObject(object);
        View view = segment.getBoundedView().getView();
        destroyItem(object);
        container.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        Segment segment = retrieveSegmentFromObject(object);
        SegmentViewHolder boundedView = segment.getBoundedView();
        return boundedView != null && boundedView.getView() == view;
    }

    public Segment<?, ?> getPrimaryItem() {
        return primaryItem;
    }


    public abstract Object instantiateItem(int position);

    public void destroyItem(Object object) {
        Segment<?, ?> segment = retrieveSegmentFromObject(object);
        segment.onStop();
        segment.unBindView();
    }

    public boolean handleBackPressed() {
        if (primaryItem == null) {
            return false;
        }
        return primaryItem.handleBackPressed();
    }

    public void onActivityResult(int code, int resultCode, Intent data) {
        if(primaryItem != null) {
            primaryItem.onActivityResult(code,resultCode,data);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        if(primaryItem != null) {
            primaryItem.onConfigurationChanged(configuration);
        }
    }


    public void onRequestPermissionsResult(int code, String[] permissions, int[] grantResults) {
        if(primaryItem != null)
            primaryItem.onRequestPermissionsResult(code,permissions,grantResults);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        parentState = SegmentViewHolder.SegmentViewState.CREATE;
    }

    @Override
    public void onStart() {
        parentState = SegmentViewHolder.SegmentViewState.START;
    }

    @Override
    public void onResume() {
        parentState = SegmentViewHolder.SegmentViewState.RESUME;
        if (primaryItem != null) {
            primaryItem.onResume();
        }
    }

    @Override
    public void onPause() {
        parentState = SegmentViewHolder.SegmentViewState.PAUSE;
    }


    @Override
    public void onStop() {
        parentState = SegmentViewHolder.SegmentViewState.STOP;
    }

    @Override
    public void onDestroy() {
        parentState = SegmentViewHolder.SegmentViewState.DESTROY;
    }
}
