package com.clumob.segment;

import android.util.Log;

import com.clumob.segment.controller.SegmentController;

/**
 * Created by prashant.rathore on 08/07/18.
 */

class SubSegmentController extends SegmentController {

    public SubSegmentController() {
        super(null,null);
    }

    @Override
    public void onCreate() {
        Log.d("SEGMENTSUB", "OnCreate   -" + this.toString().split("@")[1]);
        super.onCreate();
    }

    @Override
    public void willShow() {
        Log.d("SEGMENTSUB", "OnWillShow-" + this.toString().split("@")[1]);
        super.willShow();
    }

    @Override
    public void onResume() {
        Log.d("SEGMENTSUB", "OnResume  -" + this.toString().split("@")[1]);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("SEGMENTSUB", "OnPause   -" + this.toString().split("@")[1]);
        super.onPause();
    }

    @Override
    public void willHide() {
        Log.d("SEGMENTSUB", "OnWillHide-" + this.toString().split("@")[1]);
        super.willHide();
    }

    @Override
    public void onDestroy() {
        Log.d("SEGMENTSUB", "OnDestroy -" + this.toString().split("@")[1]);
        super.onDestroy();
    }
}
