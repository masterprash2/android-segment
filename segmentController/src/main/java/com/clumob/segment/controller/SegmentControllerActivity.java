package com.clumob.segment.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.clumob.log.AppLog;
import com.clumob.segment.presenter.activity.ActivityInteractor;
import com.clumob.segment.presenter.activity.ActivityPermissionResult;
import com.clumob.segment.presenter.activity.ActivityResult;
import com.clumob.segment.presenter.util.ParcelableUtil;
import com.clumob.segment.screen.SegmentView;

/**
 * Created by prashant.rathore on 23/02/18.
 */

public abstract class SegmentControllerActivity extends Activity {

    private SegmentController segmentController;
    private SegmentView screenView;

    private ActivityInteractorImpl activityInteractor = new ActivityInteractorImpl() {

        @Override
        public void requestPermission(String[] permissions, int requestCode) {
            ActivityCompat.requestPermissions(SegmentControllerActivity.this, permissions, requestCode);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            SegmentControllerActivity.this.startActivityForResult(intent, requestCode);
        }

        @Override
        public void performBackPress() {
            SegmentControllerActivity.this.onBackPressed();
        }

        @Override
        public String getString(int stringId) {
            return SegmentControllerActivity.this.getString(stringId);
        }
    };

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.segmentController = createDefaultSegmentController(savedInstanceState);
        super.onCreate(savedInstanceState);
        attachSegment();
    }

    protected void attachSegment() {
        if (segmentController == null) {
            return;
        }
        segmentController.onCreate();
        screenView = segmentController.createView(null);
        changeView(screenView.getView(), null);
        segmentController.bindView(screenView);
    }

    private SegmentController createDefaultSegmentController(Bundle savedInstanceState) {
        SegmentInfo segmentInfo = restoreSegment(savedInstanceState);
        segmentInfo = segmentInfo == null ? provideDefaultScreenInfo() : segmentInfo;
        SegmentController segmentController = provideController(segmentInfo);
        segmentController.attach(this, LayoutInflater.from(this));
        return segmentController;
    }

    protected SegmentInfo restoreSegment(Bundle savedInstanceState) {
        byte[] segmentInfoBytes;
        if (savedInstanceState == null) {
            segmentInfoBytes = getIntent().getByteArrayExtra("SEGMENT_INFO");
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

    protected abstract SegmentInfo provideDefaultScreenInfo();

    protected SegmentInfo changeSegment(SegmentInfo segmentInfo) {
        SegmentController newController = provideController(segmentInfo);
        newController.attach(this, LayoutInflater.from(this));
        final SegmentController oldController = this.segmentController;
        final SegmentView newScreen = newController.createView(null);
        switch (oldController.currentState) {
            case CREATE:
                newController.onCreate();
                newController.bindView(newScreen);
                break;
            case START:
                newController.onCreate();
                newController.bindView(newScreen);
                newController.onStart();
                break;
            case RESUME:
                oldController.onPause();
                newController.onCreate();
                newController.bindView(newScreen);
                newController.onStart();
                newController.onResume();
                break;
            case PAUSE:
                newController.onCreate();
                newController.bindView(newScreen);
                newController.onStart();
                break;
            case STOP:
                newController.onCreate();
                newController.bindView(newScreen);
                break;
            case DESTROY:
                return segmentInfo;

        }
        changeView(newScreen.getView(), new Runnable() {
            public void run() {
                switch (oldController.currentState) {
                    case CREATE:
                        oldController.unBindView();
                        oldController.onDestroy();
                        break;
                    case PAUSE:
                    case RESUME:
                    case START:
                        oldController.onStop();
                        oldController.unBindView();
                        oldController.onDestroy();
                        break;
                    case STOP:
                        oldController.unBindView();
                        oldController.onDestroy();
                        break;
                    case DESTROY:
                        return;
                }
            }
        });
        this.segmentController = newController;
        this.screenView = newScreen;
        return oldController.getSegmentInfo();
    }

    protected void changeView(View newView, Runnable onCompleHandler) {
        setContentView(newView);
        if (onCompleHandler != null) {
            mHandler.post(onCompleHandler);
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActivityPermissionResult permissionResult = ActivityPermissionResult.builder()
                .setPermissions(permissions)
                .setRequestCode(requestCode)
                .setGrantResult(grantResults).build();

        activityInteractor.publisPermissionResult(permissionResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResult activityResult = ActivityResult.builder().setRequestCode(requestCode).setResultCode(resultCode).setData(data).build();
        activityInteractor.publishActivityResult(activityResult);
    }

    @Override
    public void onPause() {
        segmentController.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            SegmentInfo segmentInfo = segmentController.getSegmentInfo();
            byte[] marshall = ParcelableUtil.marshall(segmentInfo);
            outState.putByteArray("SEGMENT_INFO", marshall);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        segmentController.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (segmentController == null || !(segmentController.handleBackPressed() || getScreenNavigation().popBackStack())) {
            try {
                super.onBackPressed();
            } catch (Exception e) {
                finish();
                AppLog.printStack(e);
            }
        }
    }

    @Override
    public void onDestroy() {
        segmentController.onDestroy();
        this.segmentController.unBindView();
        this.screenView = null;
        super.onDestroy();
    }

    protected abstract SegmentController provideController(SegmentInfo segmentInfo);

    @NonNull
    protected abstract SegmentNavigation getScreenNavigation();

    public ActivityInteractor getActivityInteractor() {
        return activityInteractor;
    }
}
