package com.clumob.segment.empty;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentViewHolder;
import com.clumob.segment.manager.SegmentViewHolderFactory;

/**
 * Created by prashant.rathore on 07/07/18.
 */

public class EmptySegment extends Segment {

    public EmptySegment(SegmentInfo segmentInfo) {
        super(segmentInfo, new SegmentController(null, null), new SegmentViewHolderFactory() {
            @Override
            public SegmentViewHolder<?, ?> create(final Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new SegmentViewHolder<Object, SegmentController>(context, layoutInflater, parentView) {
                    @Override
                    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                        return new View(context);
                    }

                    @Override
                    protected void onBind() {
                    }

                    @Override
                    protected void onUnBind() {

                    }
                };
            }
        });
    }
}
