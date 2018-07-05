package com.clumob.segment;

import com.clumob.list.presenter.source.PresenterSource;
import com.clumob.segment.manager.pager.SegmentPagerItemFactory;
import com.clumob.segment.manager.pager.SegmentStatePagerAdapter;
import com.clumob.segment.controller.SegmentPagerItemPresenter;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class TestPagerAdapter extends SegmentStatePagerAdapter {
    public TestPagerAdapter(PresenterSource<SegmentPagerItemPresenter> dataSource, SegmentPagerItemFactory factory) {
        super(dataSource, factory);
    }
}
