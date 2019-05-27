package com.clumob.segment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.clumob.listitem.controller.source.ArraySource;
import com.clumob.listitem.controller.source.ItemController;
import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentPagerItemController;
import com.clumob.segment.controller.Storable;
import com.clumob.segment.controller.TestStore;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentManager;
import com.clumob.segment.manager.SegmentNavigation;
import com.clumob.segment.manager.SegmentViewHolder;
import com.clumob.segment.manager.SegmentViewHolderFactory;
import com.clumob.segment.support.pager.SegmentItemProvider;
import com.clumob.segment.support.pager.viewpager.SegmentPagerAdapter;
import com.clumob.segment.support.pager.viewpager.SegmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentScreenHolder extends SegmentViewHolder<Object, TestSegmentController> {

    private ViewPager viewPager;
    private SegmentPagerAdapter pagerAdapter;

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
        this.pagerAdapter = createPagerAdapter();
        this.viewPager.setAdapter(pagerAdapter);
    }

    private SegmentPagerAdapter createPagerAdapter() {
        ItemControllerSource<SegmentPagerItemController> presenterSource = createPresenterSource();
        SegmentStatePagerAdapter pagerAdapter = new SegmentStatePagerAdapter(presenterSource, createControllerFactory(), this);
        return pagerAdapter;
    }

    @Override
    public boolean handleBackPressed() {
        return pagerAdapter.handleBackPressed();
    }

    private SegmentItemProvider createControllerFactory() {
        return new SegmentItemProvider() {
            @Override
            public Segment<?, ?> provide(ItemController itemController) {
                SegmentPagerItemController pagerItemController = (SegmentPagerItemController) itemController;
                return new Segment<>(pagerItemController.viewData, createPresenter(), createScreenFactory());
            }
        };
    }


    private ItemControllerSource<SegmentPagerItemController> createPresenterSource() {
        ArraySource<SegmentPagerItemController> source = new ArraySource<>();
        source.switchItems(createSegmentList());
        return source;
    }

    private List<SegmentPagerItemController> createSegmentList() {
        ArrayList<SegmentPagerItemController> segmentInfos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            segmentInfos.add(new SegmentPagerItemController(new SegmentInfo<Storable, TestStore>(i, null)));
        }
        return segmentInfos;
    }

    @Override
    protected void onUnBind() {
    }

    private SegmentController createPresenter() {
        return new TestSegmentController(null, null);
    }

    private SegmentViewHolderFactory createScreenFactory() {
        return new SegmentViewHolderFactory() {
            @Override
            public SegmentViewHolder<?, ?> create(final Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new SegmentViewHolder(context, layoutInflater, parentView) {

                    private View oldView;

                    FrameLayout frameLayout;
                    TextView tv;
                    int color = new Random().nextInt(Integer.MAX_VALUE);

                    @Override
                    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                        return layoutInflater.inflate(R.layout.segment_pager_item, viewGroup, false);
                    }


                    @Override
                    protected void onBind() {
                        this.tv = getView().findViewById(R.id.subText);
                        this.frameLayout = getView().findViewById(R.id.frameLayout);
                        this.frameLayout.setBackgroundColor(color);
                        SegmentNavigation navigation = getNavigation(1);
                        navigation.addToBackStack(new SegmentInfo<Storable, Storable>(1, null));
                    }

                    @Override
                    protected void onUnBind() {

                    }

                    @Override
                    public SegmentManager.SegmentCallbacks getChildManagerCallbacks(int navigationId) {
                        return new SegmentManager.SegmentCallbacks() {


                            @Override
                            public Segment provideSegment(SegmentInfo segmentInfo) {
                                return new Segment(segmentInfo, new SubSegmentController(), new SubSegmentViewViewHolderFactory());
                            }

                            @Override
                            public void setSegmentView(final View view) {
                                if (oldView != view) {
                                    frameLayout.removeAllViews();
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
