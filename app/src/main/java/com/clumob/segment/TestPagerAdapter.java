package com.clumob.segment;

import com.clumob.list.presenter.source.ArraySource;
import com.clumob.list.presenter.source.PresenterSource;
import com.clumob.list.presenter.source.ViewModelPresenter;
import com.clumob.segment.pager.SegmentControllerFactory;
import com.clumob.segment.pager.SegmentStatePagerAdapter;
import com.clumob.segment.presenter.SegmentInfo;
import com.clumob.segment.presenter.Storable;
import com.clumob.segment.presenter.TestStore;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class TestPagerAdapter extends SegmentStatePagerAdapter {
    public TestPagerAdapter(PresenterSource<ViewModelPresenter<SegmentInfo<? extends Storable, ? extends Storable>>> dataSource, SegmentControllerFactory factory) {
        super(dataSource, factory);
    }
}
