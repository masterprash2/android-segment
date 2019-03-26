package com.clumob.segment.support.pager.recycler;

import com.google.android.material.tabs.TabLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * Created by prashant.rathore on 13/07/18.
 */

public class SegmentTabsHelper {

    public static void attach(final RecyclerView recyclerView,
                              final TabLayout tabLayout,
                              SnapHelper snapHelper) {

        SegmentPageChangeHelper segmentPageChangeHelper = new SegmentPageChangeHelper(snapHelper);
        segmentPageChangeHelper.attachRecyclerView(recyclerView);
        attach(tabLayout, segmentPageChangeHelper);
    }

    public static void attach(final TabLayout tabLayout, final SegmentPageChangeHelper pageChangeHelper) {
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
                tabLayout.setScrollPosition(position, offset, true);
            }
        });
    }

}
