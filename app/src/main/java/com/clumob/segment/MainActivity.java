package com.clumob.segment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentControllerActivity;
import com.clumob.segment.controller.SegmentFactory;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentNavigation;
import com.clumob.segment.interactor.SegmentInteractor;
import com.clumob.segment.interactor.SegmentViewModel;
import com.clumob.segment.screen.SegmentView;

public class MainActivity extends SegmentControllerActivity {

    @Override
    protected SegmentInfo provideDefaultScreenInfo() {
        return new SegmentInfo(0,null);
    }

    @Override
    protected SegmentController<? extends SegmentViewModel,? extends SegmentInteractor> provideController(SegmentInfo segmentInfo) {
        return new SegmentController(segmentInfo, new TestSegmentViewModel(), new SegmentInteractor(), new SegmentFactory() {
            @Override
            public SegmentView<?, ?> create(Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new TestSegmentScreen(context,layoutInflater,parentView);
            }
        });
    }

    @NonNull
    @Override
    protected SegmentNavigation getScreenNavigation() {
        return new SegmentNavigation() {
            @Override
            public SegmentInfo navigateToScreen(SegmentInfo segmentInfo) {
                return null;
            }
        };
    }



}
