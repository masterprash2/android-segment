package com.clumob.segment.controller;


import com.clumob.listitem.controller.source.ItemControllerImpl;
import com.clumob.listitem.controller.source.ItemUpdatePublisher;

/**
 * Created by prashant.rathore on 03/07/18.
 */
@Deprecated
public class SegmentPagerItemController extends ItemControllerImpl<SegmentInfo> {

    public SegmentPagerItemController(SegmentInfo viewModel) {
        super(viewModel);
    }

    @Override
    final public void onCreate(ItemUpdatePublisher publisher) {
        super.onCreate(publisher);
    }

    @Override
    final public void onAttach(Object source) {
        super.onAttach(source);
    }

    @Override
    final public void onDetach(Object source) {
        super.onDetach(source);
    }

    @Override
    final public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    final public long getId() {
        return viewData.getId();
    }

    public String getPageTitle() {
        return null;
    }
}
