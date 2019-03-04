package com.clumob.segment.support.appcompact;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.log.AppLog;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.util.ParcelableUtil;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentManager;
import com.clumob.segment.manager.SegmentNavigation;
import com.clumob.segment.manager.SegmentViewHolder;

/**
 * Created by prashant.rathore on 14/02/18.
 */

public abstract class SegmentBottomSheetDialogFragment extends BottomSheetDialogFragment implements SegmentManager.SegmentCallbacks, Dialog.OnKeyListener {

    private Segment<?, ?> segment;
    private SegmentViewHolder viewHolder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preSegmentCreate(savedInstanceState);
        this.segment = createSegment(savedInstanceState);
        segment.onCreate();
    }

    protected void preSegmentCreate(Bundle savedInstanceState) {

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
    public void onConfigurationChanged(Configuration newConfig) {
        segment.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
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
        try {
            SegmentInfo segmentInfo = segment.getSegmentInfo();
            byte[] marshall = ParcelableUtil.marshall(segmentInfo);
            outState.putByteArray("SEGMENT_INFO", marshall);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    protected SegmentInfo restoreSegmentInfo(Bundle savedInstanceState) {
        byte[] segmentInfoBytes = null;
        if (savedInstanceState == null) {
//            segmentInfoBytes = getIntent().getByteArrayExtra("SEGMENT_INFO");
        } else {
            segmentInfoBytes = savedInstanceState.getByteArray("SEGMENT_INFO");
        }
        SegmentInfo segmentInfo = null;
        try {
            if (segmentInfoBytes != null) {
                segmentInfo = ParcelableUtil.unmarshall(segmentInfoBytes, SegmentInfo.CREATOR);
            }
        } catch (Exception e) {
            AppLog.printStack(e);
        }
        return segmentInfo;
    }

    protected Segment<?, ?> createSegment(Bundle savedInstanceState) {
        SegmentInfo segmentInfo = restoreSegmentInfo(savedInstanceState);
        return provideSegment(segmentInfo == null ? provideSegmentInfo() : segmentInfo);
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
