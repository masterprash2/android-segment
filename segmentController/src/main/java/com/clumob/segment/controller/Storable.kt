package com.clumob.segment.controller

import android.os.Parcelable

interface Storable : Parcelable {
    fun creator(): Parcelable.Creator<*>
}