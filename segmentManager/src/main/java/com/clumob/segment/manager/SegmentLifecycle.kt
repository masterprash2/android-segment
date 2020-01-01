package com.clumob.segment.manager

import android.os.Bundle

/**
 * Created by prashant.rathore on 08/07/18.
 */
interface SegmentLifecycle {
    fun onCreate(savedInstance: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onSaveInstanceState(outBundle: Bundle)
    fun onStop()
    fun onDestroy()
    fun handleBackPressed(): Boolean
}