package com.clumob.segment.support.appcompact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.manager.SegmentManager;

/**
 * Created by prashant.rathore on 23/02/18.
 */

public abstract class SegmentAppCompatActivity extends AppCompatActivity implements SegmentManager.SegmentCallbacks {

    private SegmentManager segmentManager;
    private Context context;

    public SegmentManager getSegmentManager() {
        return segmentManager;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (segmentManager == null)
            segmentManager = new SegmentManager(-1, this, this, getLayoutInflater());
        super.onCreate(savedInstanceState);
        segmentManager.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        segmentManager.onStart();
    }

    @Override
    protected void onResume() {
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
    protected void onPause() {
        segmentManager.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        segmentManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
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
    protected void onDestroy() {
        segmentManager.onDestroy();
        super.onDestroy();
    }

    public Context getContext() {
        return context;
    }
}
