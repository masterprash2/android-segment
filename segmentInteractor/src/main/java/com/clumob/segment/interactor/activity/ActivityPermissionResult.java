package com.clumob.segment.interactor.activity;


import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/**
 * Created by prashant.rathore on 23/02/18.
 */
@AutoValue
public abstract class ActivityPermissionResult {

    public abstract int getRequestCode();
    public abstract ImmutableList<String> getPermissions();
    @SuppressWarnings("mutable")
    public abstract int[] getGrantResult();

    public static Builder builder() {
        return new AutoValue_ActivityPermissionResult.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setRequestCode(int code);
        public abstract Builder setPermissions(String[] permissions);
        public abstract Builder setGrantResult(int[] value);
        public abstract ActivityPermissionResult build();
    }
}
