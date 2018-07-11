package com.clumob.segment.support.pager.recycler;

import android.support.v7.widget.RecyclerView;

import com.clumob.listitem.controller.source.ItemController;
import com.clumob.recyclerview.adapter.RvViewHolder;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public interface SnapViewFinder<Controller extends ItemController, VH extends RvViewHolder<Controller>> {

    public VH findSnapView(RecyclerView.LayoutManager layoutManager);

}
