package com.clumob.segment.controller;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prashant.rathore on 02/02/18.
 */

public class SegmentInfo implements Parcelable {

    private static final String KEY_ID = "id";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_SAVED_STATE = "savedState";

    private Bundle savedState;

    int id;
    Bundle params;

    SegmentInfo() {

    }

    public SegmentInfo(int id, Bundle params) {
        this.id = id;
        this.params = params;
        this.savedState = new Bundle();
    }

    protected SegmentInfo(Parcel in) {
        id = in.readInt();
        params = in.readBundle();
        savedState = in.readBundle();
    }

    public static final Creator<SegmentInfo> CREATOR = new Creator<SegmentInfo>() {
        @Override
        public SegmentInfo createFromParcel(Parcel in) {
            return new SegmentInfo(in);
        }

        @Override
        public SegmentInfo[] newArray(int size) {
            return new SegmentInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public Bundle getParams() {
        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeBundle(params);
        parcel.writeBundle(savedState);
    }

    public Bundle getSavedState() {
        return savedState;
    }
}
