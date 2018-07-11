package com.clumob.segment.support.pager.recycler;

import android.support.v7.widget.RecyclerView;

import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.recyclerview.adapter.RvAdapter;
import com.clumob.recyclerview.adapter.ViewHolderProvider;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public class RecyclerSegmentAdapter extends RvAdapter {
    public RecyclerSegmentAdapter(ViewHolderProvider viewHolderProvider, ItemControllerSource itemControllerSource) {
        super(viewHolderProvider, itemControllerSource);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
