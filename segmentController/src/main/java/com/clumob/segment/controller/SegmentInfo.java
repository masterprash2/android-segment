package com.clumob.segment.controller;

import android.os.Parcel;

import com.clumob.segment.controller.util.ParcelableUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by prashant.rathore on 02/02/18.
 */

public class SegmentInfo<Args extends Storable, RestoreableState extends Storable> implements Storable {

    private static final String KEY_ID = "id";
    private static final String KEY_PARAMS = "params";
    private static final String KEY_SAVED_STATE = "savedState";

    int id;
    private Args arguments;
    private RestoreableState restorableSetmentState;

    SegmentInfo() {

    }

    public SegmentInfo(int id, Args args) {
        this.id = id;
        this.arguments = args;
    }

    public Args getArguments() {
        return arguments;
    }

    protected SegmentInfo(Parcel in) {
        id = in.readInt();
        try {
            arguments = readParcel(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            restorableSetmentState = readParcel(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private <T> T readParcel(Parcel in) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        T parcelable = null;
        int readByteArrayLength = in.readInt();
        if (readByteArrayLength > 0) {
            final String className = in.readString();
            byte[] dataArray = new byte[readByteArrayLength];
            in.readByteArray(dataArray);
            Constructor<?> constructor = Class.forName(className).getDeclaredConstructor();
            constructor.setAccessible(true);
            Creator<T> creator = (Creator) constructor.newInstance();
            parcelable = ParcelableUtil.unmarshall(dataArray, creator);
        }
        return parcelable;
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

    public RestoreableState getRestorableSetmentState() {
        return restorableSetmentState;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        writeToParcel(arguments, parcel);
        writeToParcel(restorableSetmentState, parcel);
    }

    public void setRestorableSegmentState(RestoreableState restorableSetmentState) {
        this.restorableSetmentState = restorableSetmentState;
    }

    private <T extends Storable> void writeToParcel(T storable, Parcel parcel) {
        if (storable != null) {
            Creator<?> creator = storable.creator();
            if(creator == null) {
                throw new NullPointerException("Creator object cannot be null for " + getClass().getName());
            }
            final Class aClass = creator.getClass();
            final String aClassName = aClass.getName();
            byte[] marshall = ParcelableUtil.marshall(storable);
            final int length = marshall.length;
            if (length > 0) {
                parcel.writeInt(length);
                parcel.writeString(aClassName);
                parcel.writeByteArray(marshall);
                return;
            }
        }
        parcel.writeInt(-1);
        return;
    }

    @Override
    public Creator<? extends SegmentInfo> creator() {
        return CREATOR;
    }

}
