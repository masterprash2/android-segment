//package com.clumob.segment.support.pager.recycler
//
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.SnapHelper
//import com.clumob.segment.support.pager.recycler.SegmentPageChangeHelper.PageChangeListner
//import com.google.android.material.tabs.TabLayout
//import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
//
///**
// * Created by prashant.rathore on 13/07/18.
// */
//object SegmentTabsHelper {
//    fun attach(recyclerView: RecyclerView,
//               tabLayout: TabLayout,
//               snapHelper: SnapHelper) {
//        val segmentPageChangeHelper = SegmentPageChangeHelper(snapHelper)
//        segmentPageChangeHelper.attachRecyclerView(recyclerView)
//        attach(tabLayout, segmentPageChangeHelper)
//    }
//
//    fun attach(tabLayout: TabLayout, pageChangeHelper: SegmentPageChangeHelper) {
//        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                pageChangeHelper.scrollToPosition(tab.position)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//        pageChangeHelper.addPageChangeListner(object : PageChangeListner {
//            override fun onPageChanged(position: Int) {
//                tabLayout.getTabAt(position)!!.select()
//            }
//
//            override fun onPageScrolled(position: Int, offset: Float) {
//                tabLayout.setScrollPosition(position, offset, true)
//            }
//        })
//    }
//}