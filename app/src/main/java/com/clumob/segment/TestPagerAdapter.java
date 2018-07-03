package com.clumob.segment;

import com.clumob.list.presenter.source.PresenterSource;
import com.clumob.segment.pager.SegmentControllerFactory;
import com.clumob.segment.pager.SegmentStatePagerAdapter;
import com.clumob.segment.presenter.SegmentPagerItemPresenter;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class TestPagerAdapter extends SegmentStatePagerAdapter {
    public TestPagerAdapter(PresenterSource<SegmentPagerItemPresenter> dataSource, SegmentControllerFactory factory) {
        super(dataSource, factory);
    }
}
