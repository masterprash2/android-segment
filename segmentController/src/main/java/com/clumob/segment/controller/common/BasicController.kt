package com.clumob.segment.controller.common

import com.clumob.segment.controller.Storable

open class BasicController(private val itemType : Int, private val  itemId : Long) : Controller {

    override fun getId(): Long = itemId
    override fun getType(): Int = itemType

    override fun bindArgs(args: Storable?) {}

    override fun onCreate() {}

    override fun onRestore(savedState: Storable?) {}

    override fun onStart() {}

    override fun onResume() {}

    override fun onPause() {}

    override fun getState(): Storable? = null

    override fun onStop() {}

    override fun onDestroy() {}
}