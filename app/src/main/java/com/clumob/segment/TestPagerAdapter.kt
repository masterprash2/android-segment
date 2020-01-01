package com.clumob.segment

import com.clumob.listitem.controller.source.ItemControllerSource
import com.clumob.segment.controller.SegmentPagerItemController
import com.clumob.segment.support.pager.SegmentItemProvider
import com.clumob.segment.support.pager.viewpager.SegmentStatePagerAdapter

/**
 * Created by prashant.rathore on 02/07/18.
 */
class TestPagerAdapter(dataSource: ItemControllerSource<SegmentPagerItemController?>?, factory: SegmentItemProvider?) : SegmentStatePagerAdapter(dataSource!!, factory!!)