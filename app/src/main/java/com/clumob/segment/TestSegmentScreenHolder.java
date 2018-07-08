package com.clumob.segment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clumob.list.presenter.source.ArraySource;
import com.clumob.list.presenter.source.PresenterSource;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentFactory;
import com.clumob.segment.manager.SegmentManager;
import com.clumob.segment.manager.SegmentNavigation;
import com.clumob.segment.manager.pager.SegmentPagerItemFactory;
import com.clumob.segment.manager.pager.SegmentStatePagerAdapter;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentPagerItemPresenter;
import com.clumob.segment.controller.SegmentPresenter;
import com.clumob.segment.controller.Storable;
import com.clumob.segment.controller.TestStore;
import com.clumob.segment.manager.SegmentViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentScreenHolder extends SegmentViewHolder<Object,TestSegmentController> {

    private ViewPager viewPager;

    public TestSegmentScreenHolder(Context context, LayoutInflater layoutInflater, ViewGroup parentView) {
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
        PresenterSource<SegmentPagerItemPresenter> presenterSource = createPresenterSource();
        SegmentStatePagerAdapter pagerAdapter = new SegmentStatePagerAdapter(presenterSource, createControllerFactory());
        return pagerAdapter;
    }

    private SegmentPagerItemFactory createControllerFactory() {
        return new SegmentPagerItemFactory() {
            @Override
            public Segment<?,?,?> create(SegmentInfo segmentInfo) {
                return new Segment<>(segmentInfo, createPresenter(), createScreenFactory());
            }
        };
    }


    private PresenterSource<SegmentPagerItemPresenter> createPresenterSource() {
        ArraySource<SegmentPagerItemPresenter> source = new ArraySource<>();
        source.switchItems(createSegmentList());
        return source;
    }

    private List<SegmentPagerItemPresenter> createSegmentList() {
        ArrayList<SegmentPagerItemPresenter> segmentInfos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            segmentInfos.add(new SegmentPagerItemPresenter(new SegmentInfo<Storable, TestStore>(i, null)));
        }
        return segmentInfos;
    }

    @Override
    protected void onUnBind() {

    }

    private SegmentController createPresenter() {
        return new TestSegmentController(null,new SegmentPresenter<Object>(null));
    }

    private SegmentFactory createScreenFactory() {
        return new SegmentFactory() {
            @Override
            public SegmentViewHolder<?,?> create(final Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new SegmentViewHolder(context, layoutInflater, parentView) {

                    private View oldView;

                    FrameLayout frameLayout;
                    TextView tv;
                    int color = new Random().nextInt(Integer.MAX_VALUE);

                    @Override
                    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                        return layoutInflater.inflate(R.layout.segment_pager_item,viewGroup,false);
                    }


                    @Override
                    protected void onBind() {
                        this.tv = getView().findViewById(R.id.subText);
                        this.frameLayout = getView().findViewById(R.id.frameLayout);
                        this.frameLayout.setBackgroundColor(color);
                        SegmentNavigation navigation = getNavigation(1);
                        navigation.navigateToScreen(new SegmentInfo<Storable, Storable>(1,null));
                    }

                    @Override
                    protected void onUnBind() {

                    }

                    @Override
                    public SegmentManager.SegmentCallbacks getChildManagerCallbacks(int navigationId) {
                        return new SegmentManager.SegmentCallbacks() {


                            @Override
                            public Segment provideSegment(SegmentInfo segmentInfo) {
                                return new Segment(segmentInfo,new SubSegmentController(),new SubSegmentViewFactory());
                            }

                            @Override
                            public void setSegmentView(final View view) {
                                if(oldView != view) {
                                    frameLayout.removeView(oldView);
                                    frameLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            frameLayout.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                        }
                                    });

                                }
                                oldView = view;
                            }

                            @Override
                            public SegmentNavigation createSegmentNavigation(SegmentManager segmentManager) {
                                return new SegmentNavigation(segmentManager) {
                                };
                            }
                        };
                    }
                };
            }
        };
    }


}
