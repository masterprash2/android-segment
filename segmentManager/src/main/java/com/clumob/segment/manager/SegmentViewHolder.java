package com.clumob.segment.manager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.Storable;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;


public abstract class SegmentViewHolder<VM, Controller extends SegmentController> {

    protected void onConfigurationChanged(Configuration newConfig) {
        for(SegmentManager segmentManager: segmentManagers.values()) {
            segmentManager.onConfigurationChanged(newConfig);
        }
    }

    public enum SegmentViewState {
        FRESH,
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    private final Context context;
    private Controller controller;
    private VM viewModel;
    private final LayoutInflater layoutInflater;
    private final View view;
    private List<SegmentLifecycle> segmentLifecycleListeners = new LinkedList<>();

    private SegmentViewState currentState = SegmentViewState.FRESH;
    private boolean attachedToWindow = false;

    private LinkedHashMap<Integer, SegmentManager> segmentManagers = new LinkedHashMap<>();

    private final static String KEY_SAVE_STATE = "saveViewState";

    public SegmentViewHolder(Context context, LayoutInflater layoutInflater, ViewGroup parentView) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.view = createView(layoutInflater, parentView);
        this.view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                attachedToWindow = true;
                onAttachedToWindow();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                attachedToWindow = false;
                onDetachedFromWindow();
            }
        });
    }

    public boolean isAttachedToWindow() {
        return attachedToWindow;
    }

    protected void onAttachedToWindow() {

    }

    protected void onDetachedFromWindow() {

    }

    public Context getContext() {
        return context;
    }

    public final View getView() {
        return this.view;
    }

    public VM getViewModel() {
        return viewModel;
    }

    public Controller getController() {
        return controller;
    }

    protected abstract View createView(LayoutInflater layoutInflater, ViewGroup viewGroup);


    void bind(Segment<?, ?> segment, VM viewModel, Controller controller) {
        currentState = SegmentViewState.CREATE;
        this.viewModel = viewModel;
        this.controller = controller;
        for (SegmentLifecycle lifecycle : segmentLifecycleListeners) {
            lifecycle.onCreate(null);
        }
        onBind();
    }

    protected abstract void onBind();

    public final void onStart() {
        currentState = SegmentViewState.START;
        for (SegmentLifecycle lifecycle : segmentLifecycleListeners) {
            lifecycle.onStart();
        }
    }

    public final void resume() {
        currentState = SegmentViewState.RESUME;
        for (SegmentLifecycle lifecycle : segmentLifecycleListeners) {
            lifecycle.onResume();
        }
    }

    public final void pause() {
        currentState = SegmentViewState.PAUSE;
        for (SegmentLifecycle lifecycle : segmentLifecycleListeners) {
            lifecycle.onPause();
        }
    }

    public Storable createStateSnapshot() {
        return null;
    }

    public final void onStop() {
        currentState = SegmentViewState.STOP;
        for (SegmentLifecycle lifecycle : segmentLifecycleListeners) {
            lifecycle.onStop();
        }
    }

    public final void unBind() {
        currentState = SegmentViewState.DESTROY;
        for (SegmentLifecycle lifecycle : segmentLifecycleListeners) {
            lifecycle.onDestroy();
        }
        onUnBind();
    }

    protected abstract void onUnBind();


    public void onActivityResult(int code, int resultCode, Intent data) {
        for(SegmentManager segmentManager : segmentManagers.values()) {
            segmentManager.onActivityResult(code,resultCode,data);
        }
    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] grantResults) {
        for(SegmentManager segmentManager : segmentManagers.values()) {
            segmentManager.onRequestPermissionsResult(code,permissions,grantResults);
        }
    }

    final public SegmentNavigation getNavigation(int navigationId) {
        SegmentManager segmentManager = this.segmentManagers.get(navigationId);
        if (segmentManager == null) {
            segmentManager = createManagerInternal(navigationId, null);
        }
        return segmentManager.getNavigation();
    }

    public SegmentManager.SegmentCallbacks getChildManagerCallbacks(int navigationId) {
        return null;
    }

    public LayoutInflater getLayoutInflater() {
        return layoutInflater;
    }


    private SegmentManager createManagerInternal(int managerId, Bundle savedInstance) {
        SegmentManager manager = new SegmentManager(managerId, context, getChildManagerCallbacks(managerId),getLayoutInflater());
        this.segmentManagers.put(0, manager);
        this.registerLifecycleListener(manager);
        switch (this.currentState) {
            case FRESH:
                break;
            case CREATE:
                manager.onCreate(savedInstance);
                break;
            case START:
                manager.onCreate(savedInstance);
                manager.onStart();
                break;
            case RESUME:
                manager.onCreate(savedInstance);
                manager.onStart();
                manager.onResume();
                break;
            case PAUSE:
                manager.onCreate(savedInstance);
                manager.onStart();
                break;
            case STOP:
                manager.onCreate(savedInstance);
                break;
            case DESTROY:
                break;
        }
        return manager;
    }

    public void registerLifecycleListener(SegmentLifecycle listener) {
        this.segmentLifecycleListeners.add(0, listener);
    }

    public void unRegisterLifecycleListener(SegmentLifecycle lifecycle) {
        this.segmentLifecycleListeners.remove(lifecycle);
    }

    public boolean handleBackPressed() {
        for (SegmentLifecycle lifecycle : segmentLifecycleListeners) {
            if (lifecycle.handleBackPressed()) {
                return true;
            }
        }
        return false;
    }

}
