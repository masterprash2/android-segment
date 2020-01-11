package com.clumob.segment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.clumob.listitem.controller.source.ArraySource
import com.clumob.listitem.controller.source.ItemController
import com.clumob.listitem.controller.source.ItemControllerSource
import com.clumob.segment.controller.SegmentController
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.controller.SegmentPagerItemController
import com.clumob.segment.controller.list.SegmentItemController
import com.clumob.segment.manager.*
import com.clumob.segment.manager.SegmentManager.SegmentCallbacks
import com.clumob.segment.support.pager.SegmentItemProvider
import com.clumob.segment.support.pager.viewpager.SegmentPagerAdapter
import com.clumob.segment.support.pager.viewpager.SegmentStatePagerAdapter
import java.util.*

/**
 * Created by prashant.rathore on 20/06/18.
 */
class TestSegmentScreenHolder(context: Context?, layoutInflater: LayoutInflater?, parentView: ViewGroup?) : SegmentViewHolder<Any?, TestSegmentController?>(context!!, layoutInflater!!, parentView) {
    private val viewPager: ViewPager
    private var pagerAdapter: SegmentPagerAdapter? = null
    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
        return layoutInflater.inflate(R.layout.activity_main, viewGroup, false)
    }

    override fun onBind() {
        pagerAdapter = createPagerAdapter()
        pagerAdapter!!.attachLifecycleOwner(this)
        viewPager.adapter = pagerAdapter
    }

    private fun createPagerAdapter(): SegmentPagerAdapter {
        val presenterSource = createPresenterSource()
        return SegmentStatePagerAdapter(presenterSource, createControllerFactory())
    }

    override fun handleBackPressed(): Boolean {
        return pagerAdapter!!.handleBackPressed()
    }

    private fun createControllerFactory(): SegmentItemProvider {
        return object : SegmentItemProvider {
            override fun provide(itemController: SegmentItemController): Segment<*, *> {
                val pagerItemController = itemController as SegmentPagerItemController
                return Segment(pagerItemController.segmentInfo, createPresenter(), createScreenFactory())
            }
        }
    }

    private fun createPresenterSource(): ItemControllerSource<SegmentPagerItemController> {
        val source = ArraySource<SegmentPagerItemController>()
        source.switchItems(createSegmentList())
        return source
    }

    private fun createSegmentList(): List<SegmentPagerItemController> {
        val segmentInfos = ArrayList<SegmentPagerItemController>()
        for (i in 0..99) {
            segmentInfos.add(SegmentPagerItemController(SegmentInfo(i, null)))
        }
        return segmentInfos
    }

    override fun onUnBind() {
        pagerAdapter!!.detachLifeCycleOwner()
    }

    private fun createPresenter(): SegmentController<Any?> {
        return TestSegmentController(null, null)
    }

    private fun createScreenFactory(): SegmentViewHolderFactory {
        return object : SegmentViewHolderFactory {
            override fun create(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?): SegmentViewHolder<*, *> {
                return object : SegmentViewHolder<Any?, SegmentController<Any?>>(context, layoutInflater, parentView) {
                    private var oldView: View? = null
                    var frameLayout: FrameLayout? = null
                    var tv: TextView? = null
                    var color = Random().nextInt(Int.MAX_VALUE)
                    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
                        return layoutInflater.inflate(R.layout.segment_pager_item, viewGroup, false)
                    }

                    override fun onBind() {
                        tv = view.findViewById(R.id.subText)
                        frameLayout = view.findViewById(R.id.frameLayout)
                        frameLayout!!.setBackgroundColor(color)
                        val navigation = getNavigation(1)
                        navigation!!.addToBackStack(SegmentInfo(1, null))
                    }

                    override fun onUnBind() {}
                    override fun createChildManagerCallbacks(navigationId: Int): SegmentCallbacks? {
                        return object : SegmentCallbacks {
                            override fun provideSegment(segmentInfo: SegmentInfo): Segment<*, *> {
                                return Segment<Any?, SegmentController<Any?>>(segmentInfo, SubSegmentController(), SubSegmentViewViewHolderFactory())
                            }

                            override fun setSegmentView(view: View) {
                                if (oldView !== view) {
                                    frameLayout!!.removeAllViews()
                                    frameLayout!!.post { frameLayout!!.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) }
                                }
                                oldView = view
                            }

                            override fun createSegmentNavigation(segmentManager: SegmentManager): SegmentNavigation {
                                return SegmentNavigation(segmentManager)
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        viewPager = view.findViewById(R.id.viewPager)
    }
}