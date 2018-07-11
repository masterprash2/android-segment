package com.clumob.segment;

import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.segment.controller.SegmentPagerItemPresenter;
import com.clumob.segment.support.pager.SegmentProvider;
import com.clumob.segment.support.pager.viewpager.SegmentStatePagerAdapter;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class TestPagerAdapter extends SegmentStatePagerAdapter {
    public TestPagerAdapter(ItemControllerSource<SegmentPagerItemPresenter> dataSource, SegmentProvider factory) {
        super(dataSource, factory);
    }
}
