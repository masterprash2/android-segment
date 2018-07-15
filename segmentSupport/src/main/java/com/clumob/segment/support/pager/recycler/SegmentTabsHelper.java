package com.clumob.segment.support.pager.recycler;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.recyclerview.adapter.RvAdapter;
import com.clumob.segment.controller.list.SegmentItemController;

/**
 * Created by prashant.rathore on 13/07/18.
 */

public class SegmentTabsHelper {

    public static void setup(final RecyclerView recyclerView,
                             ItemControllerSource<SegmentItemController> itemSource,
                             final TabLayout tabLayout,
                             SnapHelper snapHelper) {

        SegmentPageChangeHelper segmentPageChangeHelper = new SegmentPageChangeHelper(snapHelper);
        segmentPageChangeHelper.attachRecyclerView(recyclerView);
        setup(itemSource,tabLayout,segmentPageChangeHelper);
    }

    public static void setup(ItemControllerSource<SegmentItemController> itemSource, final TabLayout tabLayout, final SegmentPageChangeHelper pageChangeHelper) {
        for(int i = 0 ; i < itemSource.getItemCount() ; i++) {
            tabLayout.addTab(tabLayout.newTab().setText("Index "+ i));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pageChangeHelper.scrollToPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pageChangeHelper.addPageChangeListner(new SegmentPageChangeHelper.PageChangeListner() {
            @Override
            public void onPageChanged(final int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrolled(int position, float offset) {
                tabLayout.setScrollPosition(position,offset,true);
            }
        });
    }

}
