package com.clumob.segment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.screen.SegmentView;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentScreen extends SegmentView<TestSegmentViewModel, TestSegmentInteractor> {


    public TestSegmentScreen(Context context, LayoutInflater layoutInflater, ViewGroup parentView) {
        super(context, layoutInflater, parentView);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.activity_main,viewGroup,false);
    }

    @Override
    protected void onBind() {

    }

    @Override
    protected void onUnBind() {

    }
}
