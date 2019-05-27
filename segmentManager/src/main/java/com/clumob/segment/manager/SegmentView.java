package com.clumob.segment.manager;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SegmentView extends FrameLayout {

    private Segment<?, ?> segment;
    private Segment.SegmentState segmentState = Segment.SegmentState.FRESH;

    public SegmentView(Context context) {
        super(context);
    }

    public SegmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SegmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SegmentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSegment(Segment<?, ?> segment) {
        if (this.segment != segment) {
            if (this.segment != null) {
                if(segment.getBoundedView() != null) {
                    removeView(segment.getBoundedView().getView());
                }
                segment.onDestroy();
            }
            this.segment = segment;
            if (this.segment != null) {
                this.segment.attach(getContext(), LayoutInflater.from(getContext()));
                this.segment.onCreate();
                SegmentViewHolder view = this.segment.createView(this);
                this.segment.bindView(view);
                addView(view.getView(), LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
                updateSegmentState(this.segmentState);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        updateSegmentState(Segment.SegmentState.RESUME);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        updateSegmentState(Segment.SegmentState.PAUSE);
        super.onDetachedFromWindow();
    }

    private void updateSegmentState(Segment.SegmentState state) {
        this.segmentState = state;
        if (this.segment == null) {
            return;
        }
        switch (state) {
            case FRESH:
            case CREATE:
                this.segment.onCreate();
                break;
            case START:
                this.segment.onStart();
                break;
            case RESUME:
                this.segment.onResume();
                break;
            case PAUSE:
                this.segment.onPause();
                break;
            case STOP:
                this.segment.onStop();
                break;
            case DESTROY:
                this.segment.onDestroy();
                break;
        }
    }


}
