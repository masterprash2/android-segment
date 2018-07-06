package com.clumob.segment.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.clumob.log.AppLog;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.activity.ActivityInteractor;
import com.clumob.segment.controller.util.ParcelableUtil;

/**
 * Created by prashant.rathore on 23/02/18.
 */

public abstract class SegmentManagerActivity extends Activity implements SegmentManager.SegmentCallbacks {

    private ActivityInteractor activityInteractor = new ActivityInteractorImpl() {

        @Override
        public void requestPermission(String[] permissions, int requestCode) {
            ActivityCompat.requestPermissions(SegmentManagerActivity.this, permissions, requestCode);
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            SegmentManagerActivity.this.startActivityForResult(intent, requestCode);
        }

        @Override
        public void performBackPress() {
            SegmentManagerActivity.this.onBackPressed();
        }

        @Override
        public String getString(int stringId) {
            return SegmentManagerActivity.this.getString(stringId);
        }
    };

    private SegmentManager segmentManager = new SegmentManager(this, this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        segmentManager.onPreCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        segmentManager.onPostCreate();
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
        segmentManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        segmentManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        segmentManager.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        segmentManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        segmentManager.onStop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (!segmentManager.handleBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        segmentManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public ActivityInteractor getActivityInteractor() {
        return activityInteractor;
    }

    public SegmentInfo changeSegment(SegmentInfo segmentInfo) {
        return segmentManager.changeSegment(segmentInfo);
    }
}
