package com.clumob.segment;

import android.util.Log;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.Storable;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentController extends SegmentController<Object> {
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
    public void willShow() {
        Log.d("SEGMENT", "OnWillShow-" + this.toString().split("@")[1]);
        super.willShow();
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
    public void willHide() {
        Log.d("SEGMENT", "OnWillHide-" + this.toString().split("@")[1]);
        super.willHide();
    }

    @Override
    public void onDestroy() {
        Log.d("SEGMENT", "OnDestroy -" + this.toString().split("@")[1]);
        super.onDestroy();
    }
}
