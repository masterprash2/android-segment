package com.clumob.segment.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.activity.ActivityInteractor;
import com.clumob.segment.controller.activity.ActivityPermissionResult;
import com.clumob.segment.controller.activity.ActivityResult;
import com.clumob.segment.view.SegmentViewHolder;

/**
 * Created by prashant.rathore on 14/02/18.
 */

public abstract class SegmentDialogFragment<VM, SP extends SegmentController<VM, ?>> extends DialogFragment {

    private Segment<VM, ?, SP> segment;
    private SegmentViewHolder<VM, SP> screenView;
    private View.OnKeyListener backPressListener;

    private ActivityInteractorImpl activityInteractor = new ActivityInteractorImpl() {

        @Override
        public void requestPermission(String[] permissions, int code) {
            SegmentDialogFragment.this.requestPermissions(permissions, code);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            SegmentDialogFragment.this.startActivityForResult(intent, requestCode);
        }

        @Override
        public void performBackPress() {
            SegmentDialogFragment.this.getActivity().onBackPressed();
        }

        @Override
        public String getString(int stringId) {
            return SegmentDialogFragment.this.getString(stringId);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.segment = provideController();
        this.segment.attach(context, LayoutInflater.from(context));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        segment.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        screenView = segment.createView(null);
        return screenView.getView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return segment.handleBackPressed();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        segment.bindView(screenView);
    }

    @Override
    public void onStart() {
        super.onStart();
        segment.onStart();
    }

    @Override
    public void onResume() {
        segment.onResume();
        super.onResume();
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
        this.getView().setOnKeyListener(null);
        this.segment.unBindView();
        this.screenView = null;
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
        ActivityPermissionResult permissionResult = ActivityPermissionResult.builder()
                .setPermissions(permissions)
                .setRequestCode(requestCode)
                .setGrantResult(grantResults).build();

        activityInteractor.publisPermissionResult(permissionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ActivityResult activityResult = ActivityResult.builder().setRequestCode(requestCode).setResultCode(resultCode).setData(data).build();
            activityInteractor.publishActivityResult(activityResult);
        }
    }


    public ActivityInteractor getActivityInteractor() {
        return activityInteractor;
    }

    protected abstract Segment<VM, ?, SP> provideController();
}
