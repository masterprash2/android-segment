package com.clumob.segment

import android.util.Log
import com.clumob.segment.controller.common.BasicController

/**
 * Created by prashant.rathore on 08/07/18.
 */
internal class SubSegmentController : BasicController(1,1L) {

    override fun onCreate() {
        super.onCreate()
        Log.d("SEGMENTSUB", "OnCreate   -" + this.toString().split("@").toTypedArray()[1])
    }

    override fun onStart() {
        Log.d("SEGMENTSUB", "OnStart-" + this.toString().split("@").toTypedArray()[1])
        super.onStart()
    }

    override fun onResume() {
        Log.d("SEGMENTSUB", "OnResume  -" + this.toString().split("@").toTypedArray()[1])
        super.onResume()
    }

    override fun onPause() {
        Log.d("SEGMENTSUB", "OnPause   -" + this.toString().split("@").toTypedArray()[1])
        super.onPause()
    }

    override fun onStop() {
        Log.d("SEGMENTSUB", "OnStop-" + this.toString().split("@").toTypedArray()[1])
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("SEGMENTSUB", "OnDestroy -" + this.toString().split("@").toTypedArray()[1])
        super.onDestroy()
    }
}