package com.clumob.segment.support.appcompact

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.clumob.segment.manager.SegmentManager
import com.clumob.segment.manager.SegmentManager.SegmentCallbacks

/**
 * Created by prashant.rathore on 23/02/18.
 */
abstract class SegmentAppCompatActivity : AppCompatActivity(), SegmentCallbacks {
    var segmentManager: SegmentManager? = null
        private set
    val context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (segmentManager == null) segmentManager = SegmentManager(-1, this, this, layoutInflater)
        super.onCreate(savedInstanceState)
        segmentManager!!.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        segmentManager!!.onStart()
    }

    override fun onResume() {
        segmentManager!!.onResume()
        super.onResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        segmentManager!!.onConfigurationChanged(newConfig)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        segmentManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        segmentManager!!.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        segmentManager!!.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        segmentManager!!.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        segmentManager!!.onStop()
        super.onStop()
    }

    override fun onBackPressed() {
        if (!segmentManager!!.handleBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        segmentManager!!.onDestroy()
        super.onDestroy()
    }

}