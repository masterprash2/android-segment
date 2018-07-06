package com.clumob.segment.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class SegmentManager<SI extends SegmentInfo<?, ?>> {

    private Segment segment;
    private SegmentViewHolder screenView;
    private final SegmentCallbacks<SI> callbacks;

    private Handler mHandler = new Handler();

    private Activity activity;

    public SegmentManager(Activity activity, SegmentCallbacks<SI> callbacks) {
        this.activity = activity;
        this.callbacks = callbacks;
    }

    public void onPreCreate(@Nullable Bundle savedInstanceState) {
        this.segment = createDefaultSegmentController(savedInstanceState);
    }

    public void onPostCreate() {
        attachSegment();
    }

    protected void attachSegment() {
        if (segment == null) {
            return;
        }
        segment.onCreate();
        screenView = segment.createView(null);
        changeView(screenView.getView(), null);
        segment.bindView(screenView);
    }

    private Segment createDefaultSegmentController(Bundle savedInstanceState) {
        SI segmentInfo = (SI) restoreSegment(savedInstanceState);
        segmentInfo = segmentInfo == null ? callbacks.provideDefaultScreenInfo() : segmentInfo;
        Segment segment = callbacks.provideSegment(segmentInfo);
        segment.attach(activity, LayoutInflater.from(activity));
        return segment;
    }

    protected SegmentInfo restoreSegment(Bundle savedInstanceState) {
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

    public SI changeSegment(SI segmentInfo) {
        Segment newController = callbacks.provideSegment(segmentInfo);
        newController.attach(activity, LayoutInflater.from(activity));
        final Segment oldController = this.segment;
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
        this.segment = newController;
        this.screenView = newScreen;
        return (SI) oldController.getSegmentInfo();
    }

    protected void changeView(View newView, Runnable onCompleHandler) {
        callbacks.setSegmentView(newView);
        if (onCompleHandler != null) {
            mHandler.post(onCompleHandler);
        }
    }


    public void onStart() {
        segment.onStart();
    }

    public void onResume() {
        segment.onResume();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ActivityPermissionResult permissionResult = ActivityPermissionResult.builder()
                .setPermissions(permissions)
                .setRequestCode(requestCode)
                .setGrantResult(grantResults).build();

        callbacks.getActivityInteractor().publisPermissionResult(permissionResult);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActivityResult activityResult = ActivityResult.builder().setRequestCode(requestCode).setResultCode(resultCode).setData(data).build();
        callbacks.getActivityInteractor().publishActivityResult(activityResult);
    }

    public void onPause() {
        segment.onPause();
    }

    public void onSaveInstanceState(Bundle outState) {
        try {
            SegmentInfo segmentInfo = segment.getSegmentInfo();
            byte[] marshall = ParcelableUtil.marshall(segmentInfo);
            outState.putByteArray("SEGMENT_INFO", marshall);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        segment.onStop();
    }

    public boolean handleBackPressed() {
        return segment != null && !segment.handleBackPressed() && !callbacks.getSegmentNavigation().popBackStack();
    }

    public void onDestroy() {
        segment.onDestroy();
        this.segment.unBindView();
        this.screenView = null;
    }

    public interface SegmentCallbacks<SI extends SegmentInfo> {
        public Segment provideSegment(SI segmentInfo);

        public SegmentNavigation getSegmentNavigation();

        public ActivityInteractor getActivityInteractor();

        public void setSegmentView(View view);

        public SI provideDefaultScreenInfo();
    }
}
