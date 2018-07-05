package com.clumob.segment.controller.activity;

import android.content.Intent;

import io.reactivex.Observable;

/**
 * Created by prashant.rathore on 23/02/18.
 */

public interface ActivityInteractor {

    public Observable<? extends ActivityResult> activityResult();

    public Observable<? extends ActivityPermissionResult> permissionResults();

    public void requestPermission(String[] permissions, int requestCode);

    public void startActivityForResult(Intent intent, int requestCode);

    public void performBackPress();

    public String getString(int stringId);

}
