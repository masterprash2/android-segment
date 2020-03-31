package com.clumob.segment.controller.common

import com.clumob.segment.controller.Storable

interface Controller {

    fun getType() : Int
    fun getId() : Long
    fun bindArgs(args: Storable?)
    fun onCreate()
    fun onRestore(savedState: Storable?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun getState() : Storable?
    fun onStop()
    fun onDestroy()

}