package com.clumob.segment.manager;

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

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.activity.ActivityInteractor;
import com.clumob.segment.controller.activity.ActivityPermissionResult;
import com.clumob.segment.controller.activity.ActivityResult;
import com.clumob.segment.view.SegmentViewHolder;

/**
 * Created by prashant.rathore on 14/02/18.
 */

public abstract class SegmentFragment<VM, SP extends SegmentController<VM, ?>> extends Fragment {

    private SegmentManager<VM, ?, SP> segmentManager;
    private SegmentViewHolder<VM, SP> screenView;
    private View.OnKeyListener backPressListener;

    private ActivityInteractorImpl activityInteractor = new ActivityInteractorImpl() {

        @Override
        public void requestPermission(String[] permissions, int code) {
            SegmentFragment.this.requestPermissions(permissions, code);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            SegmentFragment.this.startActivityForResult(intent, requestCode);
        }

        @Override
        public void performBackPress() {
            SegmentFragment.this.getActivity().onBackPressed();
        }

        @Override
        public String getString(int stringId) {
            return SegmentFragment.this.getString(stringId);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.segmentManager = provideController();
        this.segmentManager.attach(context, LayoutInflater.from(context));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        segmentManager.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        screenView = segmentManager.createView(null);
        return screenView.getView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return segmentManager.handleBackPressed();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        segmentManager.bindView(screenView);
    }

    @Override
    public void onStart() {
        super.onStart();
        segmentManager.onStart();
    }

    @Override
    public void onResume() {
        segmentManager.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        segmentManager.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        segmentManager.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        this.getView().setOnKeyListener(null);
        this.segmentManager.unBindView();
        this.screenView = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        segmentManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        this.segmentManager = null;
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

    protected abstract SegmentManager<VM, ?, SP> provideController();
}
