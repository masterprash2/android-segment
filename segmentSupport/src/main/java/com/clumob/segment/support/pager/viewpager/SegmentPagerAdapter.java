package com.clumob.segment.support.pager.viewpager;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentLifecycle;
import com.clumob.segment.manager.SegmentViewHolder;

import java.lang.ref.WeakReference;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public abstract class SegmentPagerAdapter extends PagerAdapter {

    private Segment<?, ?> primaryItem;
    private SegmentViewHolder.SegmentViewState parentState = SegmentViewHolder.SegmentViewState.FRESH;
    private final LifecycleOwner lifecycleOwner;
    private LifecycleEventObserver lifecycleEventObserver;

    public SegmentPagerAdapter(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    private void observeLifecycleEvents() {
        if(this.lifecycleEventObserver != null) {
            return;
        }
        this.lifecycleEventObserver = new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                switch (event) {
                    case ON_CREATE:
                        onCreate();
                        break;
                    case ON_START:
                        onStart();
                        break;
                    case ON_RESUME:
                        onResume();
                        break;
                    case ON_PAUSE:
                        onPause();
                        break;
                    case ON_STOP:
                        onStop();
                        break;
                    case ON_DESTROY:
                        onDestroy();
                        break;
                    case ON_ANY:
                        break;
                }
            }
        };
        this.lifecycleOwner.getLifecycle().addObserver(lifecycleEventObserver);
    }

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
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        super.registerDataSetObserver(observer);
        observeLifecycleEvents();
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
        if(this.primaryItem == segment) {
            this.primaryItem = null;
        }
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


    protected void onCreate() {
        parentState = SegmentViewHolder.SegmentViewState.CREATE;
    }

    protected void onStart() {
        parentState = SegmentViewHolder.SegmentViewState.START;
    }

    protected void onResume() {
        parentState = SegmentViewHolder.SegmentViewState.RESUME;
        if (primaryItem != null) {
            primaryItem.onResume();
        }
    }

    protected void onPause() {
        parentState = SegmentViewHolder.SegmentViewState.PAUSE;
    }


    protected void onStop() {
        parentState = SegmentViewHolder.SegmentViewState.STOP;
    }

    protected void onDestroy() {
        parentState = SegmentViewHolder.SegmentViewState.DESTROY;
    }
}
