package com.clumob.segment;

import android.util.Log;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentControllerImpl;

/**
 * Created by prashant.rathore on 08/07/18.
 */

class SubSegmentController extends SegmentControllerImpl {

    public SubSegmentController() {
        super(null,null);
    }

    @Override
    public void onCreate() {
        Log.d("SEGMENTSUB", "OnCreate   -" + this.toString().split("@")[1]);
        super.onCreate();
    }

    @Override
    public void onStart() {
        Log.d("SEGMENTSUB", "OnStart-" + this.toString().split("@")[1]);
        super.onStart();
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
    public void onStop() {
        Log.d("SEGMENTSUB", "OnStop-" + this.toString().split("@")[1]);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("SEGMENTSUB", "OnDestroy -" + this.toString().split("@")[1]);
        super.onDestroy();
    }
}
