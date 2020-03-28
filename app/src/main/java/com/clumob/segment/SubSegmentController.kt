package com.clumob.segment

import android.util.Log
import com.clumob.segment.controller.SegmentControllerImpl
import com.clumob.segment.controller.Storable

/**
 * Created by prashant.rathore on 08/07/18.
 */
internal class SubSegmentController : SegmentControllerImpl() {

    override fun onCreate(args: Storable?) {
        Log.d("SEGMENTSUB", "OnCreate   -" + this.toString().split("@").toTypedArray()[1])
        super.onCreate(args)
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