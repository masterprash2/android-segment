package com.clumob.segment;

import com.clumob.list.presenter.source.Presenter;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentPresenter;
import com.clumob.segment.controller.Storable;

/**
 * Created by prashant.rathore on 20/06/18.
 */

public class TestSegmentController extends SegmentController<Object,SegmentPresenter<Object>> {
    public TestSegmentController(Storable args, SegmentPresenter<Object> presenter) {
        super(args, presenter);
    }
}
