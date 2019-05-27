package com.clumob.segment;

import android.util.Log;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentControllerImpl;
import com.clumob.segment.controller.Storable;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentController extends SegmentControllerImpl<Object> {
    public TestSegmentController(Storable args, Object viewModel) {
        super(args,viewModel);
        Log.d("SEGMENT", "");
    }

    @Override
    public void onCreate() {
        Log.d("SEGMENT", "OnCreate   -" + this.toString().split("@")[1]);
        super.onCreate();
    }

    @Override
    public void onStart() {
        Log.d("SEGMENT", "OnStart-" + this.toString().split("@")[1]);
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("SEGMENT", "OnResume  -" + this.toString().split("@")[1]);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("SEGMENT", "OnPause   -" + this.toString().split("@")[1]);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("SEGMENT", "OnStop-" + this.toString().split("@")[1]);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("SEGMENT", "OnDestroy -" + this.toString().split("@")[1]);
        super.onDestroy();
    }
}
