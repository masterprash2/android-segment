package com.clumob.segment.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.Storable;


public abstract class SegmentViewHolder<VM, Controller extends SegmentController> {

    private final Context context;
    private Controller controller;
    private VM viewModel;
    private final LayoutInflater layoutInflater;
    private final View view;

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

    public void bind(VM viewModel, Controller controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        onBind();
    }

    protected abstract void onBind();

    public void willShow() {

    }

    public void resume() {

    }

    public void pause() {

    }

    public Storable createStateSnapshot() {
        return null;
    }

    public void willHide() {

    }

    public final void unBind() {
        onUnBind();
    }

    protected abstract void onUnBind();

}
