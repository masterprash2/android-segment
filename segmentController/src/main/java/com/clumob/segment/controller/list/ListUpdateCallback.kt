package com.clumob.segment.controller.list

interface ListUpdateCallback {
    fun onInserted(var1: Int, var2: Int)
    fun onRemoved(var1: Int, var2: Int)
    fun onMoved(var1: Int, var2: Int)
    fun onChanged(var1: Int, var2: Int, var3: Any)
}