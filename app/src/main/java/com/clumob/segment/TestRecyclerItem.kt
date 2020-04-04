package com.clumob.segment

import android.util.Log
import com.clumob.segment.controller.Storable
import com.clumob.segment.controller.common.Controller

class TestRecyclerItem(val index: Int) : Controller {

    override fun getType(): Int = index

    override fun getId(): Long = index.toLong()

    override fun onCreate() {
        Log.d("SEGMENTRV", "OnCreate $index - " + this.toString().split("@").toTypedArray()[1])
    }

    override fun onRestore(savedState: Storable?) {
        TODO("Not yet implemented")
    }

    override fun onStart() {
        Log.d("SEGMENTRV", "OnStart $index - " + this.toString().split("@").toTypedArray()[1])
    }

    override fun onResume() {
        Log.d("SEGMENTRV", "OnResume $index - " + this.toString().split("@").toTypedArray()[1])
    }

    override fun onPause() {
        Log.d("SEGMENTRV", "OnPause $index - " + this.toString().split("@").toTypedArray()[1])
    }

    override fun getState(): Storable? {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        Log.d("SEGMENTRV", "OnStop $index - " + this.toString().split("@").toTypedArray()[1])
    }

    override fun onDestroy() {
        Log.d("SEGMENTRV", "OnDestroy $index - " + this.toString().split("@").toTypedArray()[1])
    }


}