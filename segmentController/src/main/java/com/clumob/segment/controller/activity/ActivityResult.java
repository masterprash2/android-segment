package com.clumob.segment.controller.activity;


import android.content.Intent;

import com.google.auto.value.AutoValue;

import io.reactivex.annotations.Nullable;

/**
 * Created by prashant.rathore on 23/02/18.
 */
@AutoValue
public abstract class ActivityResult {

    public abstract int getRequestCode();

    public abstract int getResultCode();

    @SuppressWarnings("mutable")
    @Nullable
    public abstract Intent getData();

    public static Builder builder() {
        return new AutoValue_ActivityResult.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setRequestCode(int code);

        public abstract Builder setResultCode(int resultCode);

        public abstract Builder setData(Intent data);

        public abstract ActivityResult build();
    }
}
