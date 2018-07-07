package com.clumob.segment.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.Storable;

import java.util.LinkedHashMap;


public abstract class SegmentViewHolder<VM, Controller extends SegmentController> {


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

    private SegmentViewState currentState;

    private LinkedHashMap<Integer, SegmentManager> segmentManagers = new LinkedHashMap<>();

    private final static String KEY_SAVE_STATE = "saveViewState";

    public SegmentViewHolder(Context context, LayoutInflater layoutInflater, ViewGroup parentView) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.view = createView(layoutInflater, parentView);
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

    void bind(Segment<?, ?, ?> segment, VM viewModel, Controller controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        onBind();
        for (SegmentManager manager : segmentManagers.values()) {
            manager.onPreCreate(null);
            manager.onPostCreate();
        }
    }

    protected abstract void onBind();

    public void willShow() {
        for (SegmentManager manager : segmentManagers.values()) {
            manager.onStart();
        }
    }

    public void resume() {
        for (SegmentManager manager : segmentManagers.values()) {
            manager.onResume();
        }
    }

    public void pause() {
        for (SegmentManager manager : segmentManagers.values()) {
            manager.onPause();
        }
    }

    public Storable createStateSnapshot() {
        return null;
    }

    public void willHide() {
        for (SegmentManager manager : segmentManagers.values()) {
            manager.onStop();
        }
    }

    public final void unBind() {
        for (SegmentManager manager : segmentManagers.values()) {
            manager.onDestroy();
        }
        onUnBind();
    }

    protected abstract void onUnBind();


    public void onActivityResult(int code, int resultCode, Intent data) {

    }

    public void onRequestPermissionsResult(int code, String[] permissions, int[] grantResults) {

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


    private SegmentManager createManagerInternal(int managerId, Bundle savedInstance) {
        SegmentManager manager = new SegmentManager(managerId, context, getChildManagerCallbacks(managerId));
        this.segmentManagers.put(0, manager);
        switch (this.currentState) {
            case FRESH:
                break;
            case CREATE:
                manager.onPreCreate(savedInstance);
                manager.onPostCreate();
                break;
            case START:
                manager.onPreCreate(savedInstance);
                manager.onPostCreate();
                manager.onStart();
                break;
            case RESUME:
                manager.onPreCreate(savedInstance);
                manager.onPostCreate();
                manager.onStart();
                manager.onResume();
                break;
            case PAUSE:
                manager.onPreCreate(savedInstance);
                manager.onPostCreate();
                manager.onStart();
                break;
            case STOP:
                manager.onPreCreate(savedInstance);
                manager.onPostCreate();
                break;
            case DESTROY:
                break;
        }
        return manager;
    }

    public boolean handleBackPressed() {
        for (SegmentManager manager : segmentManagers.values()) {
            if(manager.handleBackPressed()) {
                return true;
            }
        }
        return false;
    }

}
