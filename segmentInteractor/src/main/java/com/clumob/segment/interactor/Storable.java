package com.clumob.segment.interactor;

import android.os.Parcelable;

public interface Storable extends Parcelable {
    public Class creatorClass();
}