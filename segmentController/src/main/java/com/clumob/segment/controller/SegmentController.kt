package com.clumob.segment.controller

/**
 * Created by prashant.rathore on 05/07/18.
 */
interface SegmentController {

    fun onCreate(args: Storable?)
    fun restoreState(restorableState: Storable?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}