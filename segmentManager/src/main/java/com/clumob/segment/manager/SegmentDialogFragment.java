package com.clumob.segment.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;

/**
 * Created by prashant.rathore on 14/02/18.
 */

public abstract class SegmentDialogFragment extends DialogFragment implements SegmentManager.SegmentCallbacks, Dialog.OnKeyListener {

    private Segment<?, ?> segment;
    private SegmentViewHolder viewHolder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.segment = createSegment();
        segment.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        segment.attach(getContext(), inflater);
        viewHolder = segment.createView(container);
        return viewHolder.getView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        segment.bindView(viewHolder);
    }

    @Override
    public void onStart() {
        super.onStart();
        segment.onStart();
    }

    @Override
    public void onResume() {
        segment.onResume();
        getDialog().setOnKeyListener(this);
        super.onResume();
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
            return segment.handleBackPressed();
        }
        return false;
    }

    @Override
    public void onPause() {
        segment.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        segment.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        this.segment.unBindView();
        this.viewHolder = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        segment.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        this.segment = null;
        super.onDetach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        segment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        segment.onActivityResult(requestCode, resultCode, data);
    }

    protected Segment<?, ?> createSegment() {
        return provideSegment(provideSegmentInfo());
    }

    protected abstract SegmentInfo provideSegmentInfo();

    @Override
    final public void setSegmentView(View view) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SegmentNavigation createSegmentNavigation(SegmentManager segmentManager) {
        return null;
    }

}
