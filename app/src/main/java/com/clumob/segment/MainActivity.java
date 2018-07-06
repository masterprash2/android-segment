package com.clumob.segment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentPresenter;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentFactory;
import com.clumob.segment.manager.SegmentAppCompatActivity;
import com.clumob.segment.manager.SegmentNavigation;
import com.clumob.segment.view.SegmentViewHolder;

public class MainActivity extends SegmentAppCompatActivity {


    @Override
    public Segment provideSegment(SegmentInfo segmentInfo) {
        return new Segment(segmentInfo, new SegmentController(segmentInfo.getArguments(), new SegmentPresenter(null)), new SegmentFactory() {
            @Override
            public SegmentViewHolder<?, ?> create(Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new TestSegmentScreenHolder(context, layoutInflater, parentView);
            }
        });
    }

    @Override
    public SegmentNavigation getSegmentNavigation() {
        return new SegmentNavigation() {
            @Override
            public SegmentInfo navigateToScreen(SegmentInfo segmentInfo) {
                return changeSegment(segmentInfo);
            }
        };
    }

    @Override
    public void setSegmentView(View view) {
        setContentView(view);
    }

    @Override
    public SegmentInfo provideDefaultScreenInfo() {
        return new SegmentInfo(0, null);
    }


}
