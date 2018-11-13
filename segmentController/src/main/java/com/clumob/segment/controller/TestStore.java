package com.clumob.segment.controller;

import com.google.auto.value.AutoValue;

/**
 * Created by prashant.rathore on 02/07/18.
 */
@AutoValue
public abstract class TestStore implements Storable {

    public abstract int getColor();

    @Override
    public Creator<?> creator() {
        return AutoValue_TestStore.CREATOR;
    }

    public static TestStore create(int color) {
        return new AutoValue_TestStore(color);
    }
}
