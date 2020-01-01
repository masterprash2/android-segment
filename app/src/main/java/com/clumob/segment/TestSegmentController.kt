package com.clumob.segment

import android.util.Log
import com.clumob.segment.controller.SegmentControllerImpl
import com.clumob.segment.controller.Storable

/**
 * Created by prashant.rathore on 20/06/18.
 */
class TestSegmentController(args: Storable?, viewModel: Any?) : SegmentControllerImpl<Any?>(args, viewModel) {
    override fun onCreate() {
        Log.d("SEGMENT", "OnCreate   -" + this.toString().split("@").toTypedArray()[1])
        super.onCreate()
    }

    override fun onStart() {
        Log.d("SEGMENT", "OnStart-" + this.toString().split("@").toTypedArray()[1])
        super.onStart()
    }

    override fun onResume() {
        Log.d("SEGMENT", "OnResume  -" + this.toString().split("@").toTypedArray()[1])
        super.onResume()
    }

    override fun onPause() {
        Log.d("SEGMENT", "OnPause   -" + this.toString().split("@").toTypedArray()[1])
        super.onPause()
    }

    override fun onStop() {
        Log.d("SEGMENT", "OnStop-" + this.toString().split("@").toTypedArray()[1])
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("SEGMENT", "OnDestroy -" + this.toString().split("@").toTypedArray()[1])
        super.onDestroy()
    }

    init {
        Log.d("SEGMENT", "")
    }
}