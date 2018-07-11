package com.clumob.segment;

import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.segment.manager.pager.SegmentPagerItemFactory;
import com.clumob.segment.manager.pager.SegmentStatePagerAdapter;
import com.clumob.segment.controller.SegmentPagerItemPresenter;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class TestPagerAdapter extends SegmentStatePagerAdapter {
    public TestPagerAdapter(ItemControllerSource<SegmentPagerItemPresenter> dataSource, SegmentPagerItemFactory factory) {
        super(dataSource, factory);
    }
}
