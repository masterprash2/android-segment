package com.clumob.segment.controller

open class SegmentControllerImpl<T>(private val args: Storable?, override val viewData: T) : SegmentController<T> {

    override fun onCreate() {}
    override fun restoreState(restorableState: Storable?) {}
    override fun onStart() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun onStop() {}
    override fun onDestroy() {}

}