package com.clumob.segment.screen;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.presenter.SegmentPresenter;
import com.clumob.segment.presenter.SegmentViewModel;


public abstract class SegmentView<VM extends SegmentViewModel, SI extends SegmentPresenter> {


    private final Context context;
    private VM viewModel;
    private SI interactor;
    private final LayoutInflater layoutInflater;
    private final View view;

    private final static String KEY_SAVE_STATE = "saveViewState";

    public SegmentView(Context context, LayoutInflater layoutInflater, ViewGroup parentView) {
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

    public SI getInteractor() {
        return interactor;
    }

    protected abstract View createView(LayoutInflater layoutInflater, ViewGroup viewGroup);

    public void bind(VM viewModel, SI interactor) {
        this.viewModel = viewModel;
        this.interactor = interactor;
        onBind();
    }

    protected abstract void onBind();

    public void restoreState(Bundle bundle) {
        if (bundle != null) {
            try {
                SparseArray<Parcelable> sparseParcelableArray = bundle.getSparseParcelableArray(KEY_SAVE_STATE);
                view.restoreHierarchyState(sparseParcelableArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void willShow() {

    }

    public void resume() {

    }

    public void pause() {

    }

    public void willHide() {

    }


    public void saveState(Bundle bundle) {
        SparseArray<Parcelable> viewState = new SparseArray<>();
        view.saveHierarchyState(viewState);
        bundle.putSparseParcelableArray(KEY_SAVE_STATE, viewState);
    }

    public final void unBind() {
        onUnBind();
        this.viewModel = null;
    }

    protected abstract void onUnBind();

}
