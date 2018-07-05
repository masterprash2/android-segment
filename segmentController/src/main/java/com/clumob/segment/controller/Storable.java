package com.clumob.segment.controller;

import android.os.Parcelable;

public interface Storable extends Parcelable {
    public Class creatorClass();
}