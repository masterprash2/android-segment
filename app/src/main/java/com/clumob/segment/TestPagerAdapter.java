package com.clumob.segment;

import androidx.lifecycle.LifecycleOwner;

import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.segment.controller.SegmentPagerItemController;
import com.clumob.segment.support.pager.SegmentItemProvider;
import com.clumob.segment.support.pager.viewpager.SegmentStatePagerAdapter;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class TestPagerAdapter extends SegmentStatePagerAdapter {
    public TestPagerAdapter(ItemControllerSource<SegmentPagerItemController> dataSource, SegmentItemProvider factory) {
        super(dataSource, factory);
    }
}
