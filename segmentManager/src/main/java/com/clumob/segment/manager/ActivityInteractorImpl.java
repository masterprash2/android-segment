package com.clumob.segment.manager;

import android.app.Activity;

import com.clumob.segment.controller.activity.ActivityInteractor;
import com.clumob.segment.controller.activity.ActivityPermissionResult;
import com.clumob.segment.controller.activity.ActivityResult;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

abstract class ActivityInteractorImpl implements ActivityInteractor {

    private PublishSubject<ActivityPermissionResult> activityPermissionResultPublisher = PublishSubject.create();
    private PublishSubject<ActivityResult> activityResultPublisher = PublishSubject.create();

    private Activity activity;


    void publisPermissionResult(ActivityPermissionResult permissionResult) {
        activityPermissionResultPublisher.onNext(permissionResult);
    }

    void publishActivityResult(ActivityResult activityResult) {
        activityResultPublisher.onNext(activityResult);
    }

    @Override
    public Observable<? extends ActivityResult> activityResult() {
        return activityResultPublisher;
    }

    @Override
    public Observable<ActivityPermissionResult> permissionResults() {
        return activityPermissionResultPublisher;
    }

    @Override
    public abstract void requestPermission(String[] permissions, int code);
}