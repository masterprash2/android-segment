package com.clumob.segment.presenter;

import com.google.auto.value.AutoValue;

/**
 * Created by prashant.rathore on 02/07/18.
 */
@AutoValue
public abstract class TestStore implements Storable {

    public abstract int getColor();

    @Override
    public Class creatorClass() {
        return AutoValue_TestStore.CREATOR.getClass();
    }

    public static TestStore create(int color) {
        return new AutoValue_TestStore(color);
    }
}
