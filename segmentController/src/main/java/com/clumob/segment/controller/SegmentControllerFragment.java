package com.clumob.segment.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.interactor.SegmentViewModel;
import com.clumob.segment.interactor.activity.ActivityInteractor;
import com.clumob.segment.interactor.activity.ActivityPermissionResult;
import com.clumob.segment.interactor.activity.ActivityResult;
import com.clumob.segment.screen.SegmentView;
import com.clumob.segment.interactor.SegmentInteractor;

/**
 * Created by prashant.rathore on 14/02/18.
 */

public abstract class SegmentControllerFragment<VM extends SegmentViewModel,SI extends SegmentInteractor> extends Fragment {

    private SegmentController<VM,SI> segmentController;
    private SegmentView<VM,SI> screenView;
    private View.OnKeyListener backPressListener;

    private ActivityInteractorImpl activityInteractor = new ActivityInteractorImpl() {

        @Override
        public void requestPermission(String[] permissions, int code) {
            SegmentControllerFragment.this.requestPermissions(permissions, code);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            SegmentControllerFragment.this.startActivityForResult(intent, requestCode);
        }

        @Override
        public void performBackPress() {
            SegmentControllerFragment.this.getActivity().onBackPressed();
        }

        @Override
        public String getString(int stringId) {
            return SegmentControllerFragment.this.getString(stringId);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.segmentController = provideController();
        this.segmentController.attach(context, LayoutInflater.from(context));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        segmentController.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        screenView = segmentController.createView(null);
        return screenView.getView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return segmentController.handleBackPressed();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        segmentController.bindView(screenView);
    }

    @Override
    public void onStart() {
        super.onStart();
        segmentController.onStart();
    }

    @Override
    public void onResume() {
        segmentController.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        segmentController.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        segmentController.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        this.getView().setOnKeyListener(null);
        this.segmentController.unBindView();
        this.screenView = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        segmentController.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        this.segmentController = null;
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

    protected abstract SegmentController<VM,SI> provideController();
}
