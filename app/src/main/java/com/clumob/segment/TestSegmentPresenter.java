package com.clumob.segment;

import com.clumob.segment.presenter.SegmentPresenter;
import com.clumob.segment.presenter.SegmentViewModel;
import com.clumob.segment.presenter.Storable;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentPresenter extends SegmentPresenter<Storable,Storable> {
    public TestSegmentPresenter(Storable storable, SegmentViewModel viewModel) {
        super(storable);
    }
}
