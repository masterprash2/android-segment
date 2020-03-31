package com.clumob.segment

import android.util.Log
import com.clumob.segment.controller.common.BasicController

/**
 * Created by prashant.rathore on 08/07/18.
 */
internal class PageController : BasicController(1,1L) {

    override fun onCreate() {
        super.onCreate()
        Log.d("SEGMENTPAGE", "OnCreate   -" + this.toString().split("@").toTypedArray()[1])
    }

    override fun onStart() {
        Log.d("SEGMENTPAGE", "OnStart-" + this.toString().split("@").toTypedArray()[1])
        super.onStart()
    }

    override fun onResume() {
        Log.d("SEGMENTPAGE", "OnResume  -" + this.toString().split("@").toTypedArray()[1])
        super.onResume()
    }

    override fun onPause() {
        Log.d("SEGMENTPAGE", "OnPause   -" + this.toString().split("@").toTypedArray()[1])
        super.onPause()
    }

    override fun onStop() {
        Log.d("SEGMENTPAGE", "OnStop-" + this.toString().split("@").toTypedArray()[1])
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("SEGMENTPAGE", "OnDestroy -" + this.toString().split("@").toTypedArray()[1])
        super.onDestroy()
    }
}