package com.clumob.segment.manager;

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
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.activity.ActivityInteractor;
import com.clumob.segment.controller.activity.ActivityPermissionResult;
import com.clumob.segment.controller.activity.ActivityResult;
import com.clumob.segment.controller.util.ParcelableUtil;
import com.clumob.segment.view.SegmentViewHolder;

/**
 * Created by prashant.rathore on 23/02/18.
 */

public abstract class SegmentActivity extends Activity {

    private SegmentManager segmentManager;
    private SegmentViewHolder screenView;

    private ActivityInteractorImpl activityInteractor = new ActivityInteractorImpl() {

        @Override
        public void requestPermission(String[] permissions, int requestCode) {
            ActivityCompat.requestPermissions(SegmentActivity.this, permissions, requestCode);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            SegmentActivity.this.startActivityForResult(intent, requestCode);
        }

        @Override
        public void performBackPress() {
            SegmentActivity.this.onBackPressed();
        }

        @Override
        public String getString(int stringId) {
            return SegmentActivity.this.getString(stringId);
        }
    };

    private Handler mHandler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.segmentManager = createDefaultSegmentController(savedInstanceState);
        super.onCreate(savedInstanceState);
        attachSegment();
    }

    protected void attachSegment() {
        if (segmentManager == null) {
            return;
        }
        segmentManager.onCreate();
        screenView = segmentManager.createView(null);
        changeView(screenView.getView(), null);
        segmentManager.bindView(screenView);
    }

    private SegmentManager createDefaultSegmentController(Bundle savedInstanceState) {
        SegmentInfo segmentInfo = restoreSegment(savedInstanceState);
        segmentInfo = segmentInfo == null ? provideDefaultScreenInfo() : segmentInfo;
        SegmentManager segmentManager = provideController(segmentInfo);
        segmentManager.attach(this, LayoutInflater.from(this));
        return segmentManager;
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
        SegmentManager newController = provideController(segmentInfo);
        newController.attach(this, LayoutInflater.from(this));
        final SegmentManager oldController = this.segmentManager;
        final SegmentViewHolder newScreen = newController.createView(null);
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
        this.segmentManager = newController;
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
        segmentManager.onStart();
    }

    @Override
    public void onResume() {
        segmentManager.onResume();
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
        segmentManager.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        try {
            SegmentInfo segmentInfo = segmentManager.getSegmentInfo();
            byte[] marshall = ParcelableUtil.marshall(segmentInfo);
            outState.putByteArray("SEGMENT_INFO", marshall);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        segmentManager.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (segmentManager == null || !(segmentManager.handleBackPressed() || getScreenNavigation().popBackStack())) {
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
        segmentManager.onDestroy();
        this.segmentManager.unBindView();
        this.screenView = null;
        super.onDestroy();
    }

    protected abstract SegmentManager provideController(SegmentInfo segmentInfo);

    @NonNull
    protected abstract SegmentNavigation getScreenNavigation();

    public ActivityInteractor getActivityInteractor() {
        return activityInteractor;
    }
}
