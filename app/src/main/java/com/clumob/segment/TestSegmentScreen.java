package com.clumob.segment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clumob.list.presenter.source.ArraySource;
import com.clumob.list.presenter.source.PresenterSource;
import com.clumob.list.presenter.source.ViewModelPresenter;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentFactory;
import com.clumob.segment.pager.SegmentControllerFactory;
import com.clumob.segment.pager.SegmentStatePagerAdapter;
import com.clumob.segment.presenter.SegmentInfo;
import com.clumob.segment.presenter.SegmentPresenter;
import com.clumob.segment.presenter.Storable;
import com.clumob.segment.presenter.TestStore;
import com.clumob.segment.screen.SegmentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentScreen extends SegmentView<TestSegmentPresenter> {

    private ViewPager viewPager;

    public TestSegmentScreen(Context context, LayoutInflater layoutInflater, ViewGroup parentView) {
        super(context, layoutInflater, parentView);
        this.viewPager = getView().findViewById(R.id.viewPager);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.activity_main, viewGroup, false);
    }

    @Override
    protected void onBind() {

        this.viewPager.setAdapter(createPagerAdapter());

    }

    private PagerAdapter createPagerAdapter() {
        PresenterSource<ViewModelPresenter<SegmentInfo<? extends Storable, ? extends Storable>>> presenterSource = createPresenterSource();
        SegmentStatePagerAdapter pagerAdapter = new SegmentStatePagerAdapter(presenterSource, createControllerFactory());
        return pagerAdapter;
    }

    private SegmentControllerFactory createControllerFactory() {
        return new SegmentControllerFactory() {
            @Override
            public SegmentController<?> create(SegmentInfo segmentInfo) {
                return new SegmentController<>(segmentInfo, createPresenter(), createScreenFactory());
            }
        };
    }


    private PresenterSource<ViewModelPresenter<SegmentInfo<? extends Storable, ? extends Storable>>> createPresenterSource() {
        ArraySource<ViewModelPresenter<SegmentInfo<? extends Storable, ? extends Storable>>> source = new ArraySource<>();
        source.switchItems(createSegmentList());
        return source;
    }

    private List<ViewModelPresenter<SegmentInfo<? extends Storable, ? extends Storable>>> createSegmentList() {
        ArrayList<ViewModelPresenter<SegmentInfo<? extends Storable, ? extends Storable>>> segmentInfos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            segmentInfos.add(new ViewModelPresenter<SegmentInfo<? extends Storable, ? extends Storable>>(new SegmentInfo<Storable, TestStore>(i, null)));
        }
        return segmentInfos;
    }

    @Override
    protected void onUnBind() {

    }

    private SegmentPresenter createPresenter() {
        return new SegmentPresenter(null);
    }

    private SegmentFactory createScreenFactory() {
        return new SegmentFactory() {
            @Override
            public SegmentView<?> create(final Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new SegmentView(context, layoutInflater, parentView) {

                    @Override
                    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                        TextView tv = new TextView(context);
                        tv.setBackgroundColor(new Random().nextInt(Integer.MAX_VALUE));
                        return tv;
                    }


                    @Override
                    protected void onBind() {

                    }

                    @Override
                    protected void onUnBind() {

                    }
                };
            }
        };
    }


}
