package com.clumob.segment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentControllerImpl;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.Storable;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentView;
import com.clumob.segment.manager.SegmentViewHolderFactory;
import com.clumob.segment.manager.SegmentManager;
import com.clumob.segment.manager.SegmentNavigation;
import com.clumob.segment.manager.SegmentViewHolder;
import com.clumob.segment.support.appcompact.SegmentAppCompatActivity;

public class MainActivity extends SegmentAppCompatActivity  {


    @Override
    public Segment provideSegment(SegmentInfo segmentInfo) {
        return new Segment(segmentInfo, new SegmentControllerImpl(segmentInfo.getArguments(), null), new SegmentViewHolderFactory() {
            @Override
            public SegmentViewHolder<?, ?> create(Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new TestSegmentScreenHolder(context, layoutInflater, parentView);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SegmentView segmentView = new SegmentView(this);
        SegmentInfo segmentInfo = new SegmentInfo(1, null);
        segmentView.setSegment(new Segment(segmentInfo, new SegmentControllerImpl(segmentInfo.getArguments(), null), new SegmentViewHolderFactory() {
            @Override
            public SegmentViewHolder<?, ?> create(Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new TestSegmentScreenHolder(context, layoutInflater, parentView);
            }
        }));
        setContentView(segmentView);
//        getSegmentManager().getNavigation().navigateToScreen(new SegmentInfo<Storable, Storable>(1, null));
    }

    @Override
    public void setSegmentView(View view) {
        setContentView(view);
    }

    @Override
    public SegmentNavigation createSegmentNavigation(final SegmentManager segmentManager) {
        return new SegmentNavigation(segmentManager) {

        };
    }

}
